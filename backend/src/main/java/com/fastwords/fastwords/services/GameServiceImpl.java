package com.fastwords.fastwords.services;

import org.springframework.stereotype.Service;

import com.fastwords.fastwords.models.dtos.CreateGameDto;
import com.fastwords.fastwords.models.dtos.GameResponseDto;
import com.fastwords.fastwords.models.entities.Collection;
import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.models.entities.User;
import com.fastwords.fastwords.repository.GameRepository;

@Service
public class GameServiceImpl implements GameService {

    private final UserService userService;
    private final CollectionService collectionService;
    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository, UserService userService, CollectionService collectionService) {
        this.collectionService = collectionService;
        this.userService = userService;
        this.gameRepository = gameRepository;
    }

    @Override
    public GameResponseDto createGame(CreateGameDto createGameDto) {
        try {

            User player1 = userService.findUserOrThrowNotFound(createGameDto.getPlayer1Id());
            User player2 = userService.findUserOrThrowNotFound(createGameDto.getPlayer2Id());
            Collection collection = collectionService.findCollectionOrThrowNotFound(createGameDto.getCollectionId());
            Game game = Game.builder()
                    .name(createGameDto.getName())
                    .player1(player1)
                    .player2(player2)
                    .collection(collection)
                    .gameStatus(createGameDto.getGameStatus())
                    .build();

            return GameResponseDto.builder()
                    .id(gameRepository.save(game).getId())
                    .name(game.getName())
                    .player1Id(player1.getId())
                    .player2Id(player2.getId())
                    .collectionId(collection.getId())
                    .gameStatus(game.getGameStatus())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error creating game: " + e.getMessage());
        }
    }

    @Override
    public void deleteGame(Long roomId, Long userId) {
        try {
            Game game = gameRepository.findById(roomId)
                    .orElseThrow(() -> new RuntimeException("Game not found with id: " + roomId));

            if (!game.getPlayer1().getId().equals(userId) && !game.getPlayer2().getId().equals(userId)) {
                throw new RuntimeException("User does not have permission to delete this game");
            }

            gameRepository.delete(game);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error deleting game: " + e.getMessage());
        }
    }

    @Override
    public GameResponseDto getGameById(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
        return GameResponseDto.builder()
                .id(game.getId())
                .name(game.getName())
                .player1Id(game.getPlayer1().getId())
                .player2Id(game.getPlayer2().getId())
                .collectionId(game.getCollection() != null ? game.getCollection().getId() : null)
                .gameStatus(game.getGameStatus())
                .timePerTurn(game.getTimePerTurn())
                .currentTurnPlayerId(game.getCurrentTurnPlayer() != null ? game.getCurrentTurnPlayer().getId() : null   )
                .build();
    }

    @Override
    public Game findGameOrThrowNotFound(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Game not found with id: " + id));
    }

}
