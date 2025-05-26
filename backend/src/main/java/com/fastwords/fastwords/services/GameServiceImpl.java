package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.CreateGameDto;
import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.repository.GameRepository;

public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public CreateGameDto createGame(CreateGameDto createGameDto) {
        try {
            Game game = Game.builder()
                    .name(createGameDto.getName())
                    .player1(createGameDto.getPlayer1Id())
                    .player2(createGameDto.getPlayer2Id())
                    .collectionId(createGameDto.getCollectionId())
                    .status(createGameDto.getStatus())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error creating game: " + e.getMessage());
        }
    }

    @Override
    public void deleteGame(Long roomId, Long userId) {
        // Implementation for deleting a game
        // This will involve checking if the user has permission to delete the game
        // and then removing it from the database.
    }

}
