package com.fastwords.fastwords.controller;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fastwords.fastwords.models.dtos.MatchmakingRequest;

@Controller
public class MatchmakingController {

    private final Queue<String> waitingPlayers = new ConcurrentLinkedQueue<>();
    private final SimpMessagingTemplate messagingTemplate;

    public MatchmakingController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/matchmaking")
    public void handleMatchmaking(MatchmakingRequest request) {
        String playerId = request.getPlayerId();

        String opponentId = waitingPlayers.poll();
        if (opponentId != null) {
            String gameId = UUID.randomUUID().toString();

            // Envía a /topic/matchmaking/{playerId}
            messagingTemplate.convertAndSend("/topic/matchmaking/" + playerId, gameId);
            messagingTemplate.convertAndSend("/topic/matchmaking/" + opponentId, gameId);
        } else {
            waitingPlayers.add(playerId);
        }
    }

}
