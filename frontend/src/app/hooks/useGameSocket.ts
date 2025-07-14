import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import { showGameStartedToast, showPlayerTurnToast } from "../utils/toast";

export function useGameSocket(gameId: string, playerId: string) {
    const [gameStarted, setGameStarted] = useState(false);
    const [lastWord, setLastWord] = useState<string | null>(null);
    const stompRef = useRef<Client | null>(null);
    const [currentTurnPlayerId, setCurrentTurnPlayerId] = useState<string | null>(null);

    useEffect(() => {
        const socketUrl = `ws://localhost:8080/ws?userId=${playerId}&gameId=${gameId}`;
        const client = new Client({
            brokerURL: socketUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log("✅ WebSocket conectado para gameId=", gameId);

                client.subscribe(`/topic/game/${gameId}/start`, (message) => {
                    if (message.body === "start") {
                        showGameStartedToast();
                        console.log("🎮 Partida iniciada");
                        setGameStarted(true);
                    }
                });
                console.log(`📡 Subscribiéndome a /topic/game/${gameId}/turn`);

                client.subscribe(`/topic/game/${gameId}/turn`, (message) => {
                    const turnPlayerId = message.body;
                    console.log("📩 Turno recibido para:", turnPlayerId);

                    showPlayerTurnToast();
                    setCurrentTurnPlayerId(turnPlayerId);
                    setLastWord(null); // si quieres limpiar la palabra anterior
                });
            },
            onStompError: (frame) => {
                console.error("❌ STOMP error:", frame);
            },
        });

        client.activate();
        stompRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [gameId, playerId]);

    const sendTurn = (word: string) => {
        if (!stompRef.current || !stompRef.current.connected) {
            console.warn("🚫 STOMP no conectado");
            return;
        }

        const turnPayload = {
            gameId,
            playerId,
            word,
        };

        stompRef.current.publish({
            destination: "/app/game/turn",
            body: JSON.stringify(turnPayload),
        });

        console.log("🚀 Turno enviado:", turnPayload);
    };

    return {
        gameStarted,
        lastWord,
        sendTurn,
        currentTurnPlayerId,
    };
}
