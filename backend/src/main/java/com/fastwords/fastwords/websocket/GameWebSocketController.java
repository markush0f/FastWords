package com.fastwords.fastwords.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {
    private final GameSocketService gameSocketService;

    public GameWebSocketController(GameSocketService gameSocketService) {
        this.gameSocketService = gameSocketService;
    }

    @MessageMapping("/game/send")
    public void onMessage(GameMessageDto message) {
        gameSocketService.processMessage(message);
    }
}
