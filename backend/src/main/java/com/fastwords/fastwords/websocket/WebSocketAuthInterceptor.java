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

        // Verifica que la petición sea HTTP tipo servlet
        if (!(request instanceof ServletServerHttpRequest servletRequest)) {
            return false;
        }

        // Extrae la petición HTTP real
        HttpServletRequest httpRequest = servletRequest.getServletRequest();
        String userIdStr = httpRequest.getParameter("userId");
        String gameIdStr = httpRequest.getParameter("gameId");

        // Si no se envia userId, se rechaza la conexión
        if (userIdStr == null) {
            System.out.println("❌ Falta userId en la URL");
            return false;
        }

        Long userId;
        Long gameId;

        // Convierte userId a Long, si es inválido rechaza
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            System.out.println("❌ formato de userId inválido: " + userIdStr);
            return false;
        }

        // Guarda el userId en los atributos de la sesión WebSocket
        attributes.put("userId", userId);

        // Si no hay gameId, se asume que es una conexión inicial para matchmaking
        if (gameIdStr == null) {
            System.out.println("🔁 Conexión sin gameId (modo matchmaking)");
            return true;
        }

        // Convierte gameId a Long, si falla rechaza
        try {
            gameId = Long.parseLong(gameIdStr);
        } catch (NumberFormatException e) {
            System.out.println("❌ formato de gameId inválido: " + gameIdStr);
            return false;
        }

        // Busca el juego por ID, si no existe rechaza
        Optional<Game> optionalGame = gameRepository.findById(gameId);
        if (optionalGame.isEmpty()) {
            System.out.println("❌ Juego no encontrado: " + gameId);
            return false;
        }

        Game game = optionalGame.get();

        // Verifica que el usuario pertenezca al juego (player1 o player2)
        boolean isPlayer
                = (game.getPlayer1() != null && game.getPlayer1().getId().equals(userId))
                || (game.getPlayer2() != null && game.getPlayer2().getId().equals(userId));

        if (!isPlayer) {
            System.out.println("❌ El usuario " + userId + " no pertenece al juego " + gameId);
            return false;
        }

        // Asegura que haya una lista para los jugadores conectados a este juego
        connectedUsers.putIfAbsent(gameId, ConcurrentHashMap.newKeySet());
        Set<Long> players = connectedUsers.get(gameId);

        // Si ya estaba conectado, se permite reconectar
        if (players.contains(userId)) {
            System.out.println("🔁 Reconexion permitida: userId=" + userId + " en gameId=" + gameId);
            attributes.put("gameId", gameId);
            return true;
        }

        // Si ya hay 2 jugadores distintos conectados, rechaza nuevos
        if (players.size() >= 2) {
            System.out.println("❌ El juego " + gameId + " ya tiene 2 jugadores distintos conectados");
            return false;
        }

        // Agrega al usuario como jugador conectado
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

        if (userIdStr == null || gameIdStr == null) {
            return;
        }

        try {
            Long userId = Long.parseLong(userIdStr);
            Long gameId = Long.parseLong(gameIdStr);
            connectionService.addConnectedPlayer(gameId, userId);
        } catch (NumberFormatException ignored) {
        }
    }
}
