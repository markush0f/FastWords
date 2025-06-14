package com.fastwords.fastwords.services;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class GameConnectionServiceImpl implements GameConnectionService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ConcurrentMap<Long, Set<Long>> connectedUsers = new ConcurrentHashMap<>();

    public GameConnectionServiceImpl(@Lazy SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void addConnectedPlayer(Long gameId, Long userId) {
        connectedUsers.putIfAbsent(gameId, ConcurrentHashMap.newKeySet());
        Set<Long> players = connectedUsers.get(gameId);
        players.add(userId);

        if (players.size() == 2) {
            new Thread(() -> {
                try {
                    Thread.sleep(300); 
                    messagingTemplate.convertAndSend("/topic/game/" + gameId, "start");
                } catch (InterruptedException ignored) {
                }
            }).start();
        }
    }
}
