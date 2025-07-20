package com.fastwords.fastwords.controller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastwords.fastwords.common.enums.GameStatus;
import com.fastwords.fastwords.models.dtos.GameResponseDto;
import com.fastwords.fastwords.models.dtos.MatchmakingRequest;
import com.fastwords.fastwords.models.entities.Collection;
import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.models.entities.User;
import com.fastwords.fastwords.repository.CollectionRepository;
import com.fastwords.fastwords.repository.GameRepository;
import com.fastwords.fastwords.repository.UserRepository;
import com.fastwords.fastwords.services.CollectionServiceImpl;
import com.fastwords.fastwords.services.GameConnectionService;

@Controller
public class MatchmakingController {

    private final Queue<String> waitingPlayers = new ConcurrentLinkedQueue<>();
    private final SimpMessagingTemplate messagingTemplate;
    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;
    private final GameConnectionService gameConnectionService;
    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    public MatchmakingController(
            SimpMessagingTemplate messagingTemplate,
            GameRepository gameRepository,
            UserRepository userRepository,
            CollectionRepository collectionRepository,
            GameConnectionService gameConnectionService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.collectionRepository = collectionRepository;
        this.gameConnectionService = gameConnectionService;
    }

    @MessageMapping("/matchmaking")
    public void handleMatchmaking(MatchmakingRequest request) throws JsonProcessingException {
        logger.info("Received collection id request from player: {}", request.getCollectionId());
        String playerId = request.getPlayerId();
        String opponentId = waitingPlayers.poll();
        logger.info("Creating game between {} and {}", playerId, opponentId);

        if (opponentId != null) {
            User player1 = userRepository.findById(Long.valueOf(playerId)).orElseThrow();
            logger.info("Player 1: {}", player1.getUsername());
            User player2 = userRepository.findById(Long.valueOf(opponentId)).orElseThrow();
            logger.info("Player 2: {}", player2.getUsername());
            Collection collection = collectionRepository.findById(request.getCollectionId()).orElse(null);

            Game game = Game.builder()
                    .name("Game between " + playerId + " and " + opponentId + " at " + System.currentTimeMillis())
                    .player1(player1)
                    .player2(player2)
                    .collection(collection)
                    .gameStatus(GameStatus.PENDING)
                    .timePerTurn(3)
                    .build();

            Game gameCreated = gameRepository.save(game);
            Long gameId = gameCreated.getId();

            messagingTemplate.convertAndSend("/topic/matchmaking/" + playerId, gameId);
            messagingTemplate.convertAndSend("/topic/matchmaking/" + opponentId, gameId);

            sendGameData(gameCreated);
        } else {

            waitingPlayers.add(playerId);
        }
    }

    @MessageMapping("/game/data/request")
    public void resendGameData(MatchmakingRequest request) throws JsonProcessingException {
        logger.info("📩 Petición explícita de gameData: gameId={}, playerId={}", request.getGameId(), request.getPlayerId());

        Long gameId = Long.parseLong(request.getGameId());
        Game game = gameRepository.findById(gameId).orElseThrow();

        sendGameData(game);
    }

    private void sendGameData(Game game) throws JsonProcessingException {
        GameResponseDto gameData = GameResponseDto.builder()
                .id(game.getId())
                .name(game.getName())
                .player1Id(game.getPlayer1().getId())
                .player2Id(game.getPlayer2().getId())
                .collectionId(game.getCollection() != null ? game.getCollection().getId() : null)
                .gameStatus(game.getGameStatus())
                .timePerTurn(game.getTimePerTurn())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(gameData);

        logger.info("🔁 Reenviando gameData a /topic/game/{}/data", game.getId());
        messagingTemplate.convertAndSend("/topic/game/" + game.getId() + "/data", json);
    }
}
