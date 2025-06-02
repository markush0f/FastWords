package com.fastwords.fastwords.websocket;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public GameSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void processMessage(GameMessageDto message) {

        String destination = "/topic/games/" + message.getGameId();
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendSystemMessage(Long gameId, String content) {
        GameMessageDto message = GameMessageDto.builder()
                .type("SYSTEM")
                .content(content)
                .sender("server")
                .gameId(gameId)
                .build();


        messagingTemplate.convertAndSend("/topic/games/" + gameId, message);
    }
}
