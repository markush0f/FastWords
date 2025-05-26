package com.fastwords.fastwords.websocket;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.repository.GameRepository;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final GameRepository gameRepository;

    // Control de conexiones activas por gameId
    private final Map<Long, Set<String>> connectedUsers = new ConcurrentHashMap<>();

    public WebSocketAuthInterceptor(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) {

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }

        HttpServletRequest httpRequest = servletRequest.getServletRequest();
        String userIdStr = httpRequest.getParameter("userId");
        String gameIdStr = httpRequest.getParameter("gameId");

        if (userIdStr == null || gameIdStr == null) {
            return false;
        }

        Long userId;
        Long gameId;
        try {
            userId = Long.parseLong(userIdStr);
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
            return false;
        }

        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            return false;
        }

        Game game = optionalGame.get();

        boolean isPlayer
                = (game.getPlayer1() != null && game.getPlayer1().getId().equals(userId))
                || (game.getPlayer2() != null && game.getPlayer2().getId().equals(userId));

        if (!isPlayer) {
            return false;
        }

        connectedUsers.putIfAbsent(gameId, ConcurrentHashMap.newKeySet());
        Set<String> players = connectedUsers.get(gameId);

        if (players.size() >= 2 && !players.contains(String.valueOf(userId))) {
            return false;
        }

        players.add(String.valueOf(userId));

        attributes.put("userId", userId);
        attributes.put("gameId", gameId);

        System.out.println("✅ WebSocket conectado: userId=" + userId + ", gameId=" + gameId);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
            @org.springframework.lang.NonNull ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {
    }
}
