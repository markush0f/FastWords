package com.fastwords.fastwords.services;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastwords.fastwords.common.enums.GameStatus;
import com.fastwords.fastwords.models.dtos.GameResponseDto;
import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.repository.GameRepository;

@Service
public class GameConnectionServiceImpl implements GameConnectionService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentMap<Long, Set<Long>> connectedUsers = new ConcurrentHashMap<>();
    private final GameService gameService;
    private final GameRepository gameRepository;
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    public GameConnectionServiceImpl(@Lazy SimpMessagingTemplate messagingTemplate,
            GameService gameService,
            GameRepository gameRepository,
            ObjectMapper objectMapper) 
        {
            this.messagingTemplate = messagingTemplate;
            this.gameService = gameService;
            this.gameRepository = gameRepository;
            this.objectMapper = objectMapper;
        }

        @Override
        public void addConnectedPlayer
        (Long gameId, Long userId
            
        ) {
        connectedUsers.putIfAbsent(gameId, ConcurrentHashMap.newKeySet());
            Set<Long> players = connectedUsers.get(gameId);
            players.add(userId);

            if (players.size() == 2) {
                new Thread(() -> {
                    try {
                        Thread.sleep(300);
                        messagingTemplate.convertAndSend("/topic/game/" + gameId + "/start", "start");
                    } catch (InterruptedException ignored) {
                    }
                }).start();
            }
        }

        @Override
        public void notifyGameStart
        (Long gameId
            
        ) {
        logger.info("🔁 Starting game with ID: {}", gameId);
            Game game = gameService.findGameOrThrowNotFound(gameId);

            if (game.getCurrentTurnPlayer() == null) {
                if (Math.random() < 0.5) {
                    game.setCurrentTurnPlayer(game.getPlayer1());
                } else {
                    game.setCurrentTurnPlayer(game.getPlayer2());
                }

                game.setGameStatus(GameStatus.ACTIVE);
                gameRepository.save(game);
            }

            try {
                GameResponseDto gameData = GameResponseDto.builder()
                        .id(game.getId())
                        .name(game.getName())
                        .player1Id(game.getPlayer1().getId())
                        .player2Id(game.getPlayer2().getId())
                        .collectionId(game.getCollection() != null ? game.getCollection().getId() : null)
                        .gameStatus(game.getGameStatus())
                        .timePerTurn(game.getTimePerTurn())
                        .build();

                String json = objectMapper.writeValueAsString(gameData);
                messagingTemplate.convertAndSend("/topic/game/" + gameId + "/data", json);
            } catch (Exception e) {
                logger.error("❌ Error sending gameData: ", e);
            }

            messagingTemplate.convertAndSend("/topic/game/" + gameId + "/start", "start");
            messagingTemplate.convertAndSend("/topic/game/" + gameId + "/turn", game.getCurrentTurnPlayer().getId().toString());
        }

    }
