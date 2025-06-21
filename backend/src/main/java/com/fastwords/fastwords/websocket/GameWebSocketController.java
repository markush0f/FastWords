package com.fastwords.fastwords.websocket;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.fastwords.fastwords.models.dtos.GameStartDto;
import com.fastwords.fastwords.models.dtos.PlayTurnDto;
import com.fastwords.fastwords.services.GameConnectionService;

@Controller
public class GameWebSocketController {

    private final GameSocketService gameSocketService;
    private final GameConnectionService gameConnectionService;

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
        gameConnectionService.notifyGameStart(Long.parseLong(gameStartDto.getGameId()));
    }

    @MessageMapping("/game/{gameId}/play")
    public void playTurn(@DestinationVariable Long gameId, PlayTurnDto playTurnDto) {
        gameSocketService.handleTurn(gameId, playTurnDto);
    }

}
