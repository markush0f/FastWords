package com.fastwords.fastwords.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.fastwords.fastwords.models.dtos.GameStartDto;
import com.fastwords.fastwords.models.dtos.PlayTurnDto;
import com.fastwords.fastwords.services.CollectionServiceImpl;
import com.fastwords.fastwords.services.GameConnectionService;

@Controller
public class GameWebSocketController {

    private final GameSocketService gameSocketService;
    private final GameConnectionService gameConnectionService;
    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    public GameWebSocketController(GameSocketService gameSocketService, GameConnectionService gameConnectionService) {
        this.gameSocketService = gameSocketService;
        this.gameConnectionService = gameConnectionService;
    }

    @MessageMapping("/game/send")
    public void onMessage(GameMessageDto message) {
        gameSocketService.processMessage(message);
    }

    @MessageMapping("/game/start")
    public void startGame(GameStartDto gameStartDto) {
        logger.info("Executing startGame with gameId: {}", gameStartDto.getGameId());
        gameConnectionService.notifyGameStart(Long.parseLong(gameStartDto.getGameId()));
    }

    @MessageMapping("/game/turn")
    public void handleTurn(PlayTurnDto turnDto) {
        System.out.println("📨 [handleTurn] gameId=" + turnDto.getGameId() + ", playerId=" + turnDto.getPlayerId() + ", word=" + turnDto.getWord());
        
        try {
            gameSocketService.handleTurn(
                    Long.parseLong(turnDto.getGameId()),
                    Long.parseLong(turnDto.getPlayerId()),
                    turnDto.getWord()
            );
        } catch (IllegalArgumentException e) {
            gameSocketService.sendSystemMessage(Long.parseLong(turnDto.getGameId()), e.getMessage());
        }
    }


}
