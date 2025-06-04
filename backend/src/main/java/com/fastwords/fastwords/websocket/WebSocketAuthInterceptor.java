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
import com.fastwords.fastwords.services.GameConnectionService;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final GameRepository gameRepository;
    private final GameConnectionService connectionService;

    private final Map<Long, Set<Long>> connectedUsers = new ConcurrentHashMap<>();

    public WebSocketAuthInterceptor(GameRepository gameRepository, GameConnectionService connectionService) {
        this.gameRepository = gameRepository;
        this.connectionService = connectionService;
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

        if (userIdStr == null) {
            System.out.println("❌ Falta userId en la URL");
            return false;
        }

        Long userId;
        Long gameId;

        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            System.out.println("❌ userId inválido");
            return false;
        }

        attributes.put("userId", userId);

        // 👉 Permitir conexiones sin gameId (modo matchmaking)
        if (gameIdStr == null) {
            System.out.println("🔁 Conexión sin gameId (modo matchmaking)");
            return true;
        }

        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
            System.out.println("❌ gameId inválido");
            return false;
        }

        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            System.out.println("❌ Juego no encontrado: " + gameId);
            return false;
        }

        Game game = optionalGame.get();

        boolean isPlayer =
                (game.getPlayer1() != null && game.getPlayer1().getId().equals(userId)) ||
                (game.getPlayer2() != null && game.getPlayer2().getId().equals(userId));

        if (!isPlayer) {
            System.out.println("❌ El usuario " + userId + " no pertenece al juego " + gameId);
            return false;
        }

        connectedUsers.putIfAbsent(gameId, ConcurrentHashMap.newKeySet());
        Set<Long> players = connectedUsers.get(gameId);

        if (players.contains(userId)) {
            System.out.println("🔁 Reconexion permitida: userId=" + userId + " en gameId=" + gameId);
            attributes.put("gameId", gameId);
            return true;
        }

        if (players.size() >= 2) {
            System.out.println("❌ El juego " + gameId + " ya tiene 2 jugadores distintos conectados");
            return false;
        }

        players.add(userId);
        attributes.put("gameId", gameId);

        System.out.println("✅ WebSocket conectado: userId=" + userId + ", gameId=" + gameId);
        System.out.println("📡 Conectados actualmente: " + players);

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception) {

        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return;
        }

        HttpServletRequest httpRequest = servletRequest.getServletRequest();
        String userIdStr = httpRequest.getParameter("userId");
        String gameIdStr = httpRequest.getParameter("gameId");

        if (userIdStr == null || gameIdStr == null) return;

        try {
            Long userId = Long.parseLong(userIdStr);
            Long gameId = Long.parseLong(gameIdStr);
            connectionService.addConnectedPlayer(gameId, userId);
        } catch (NumberFormatException ignored) {
        }
    }
}
