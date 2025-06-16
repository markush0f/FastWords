import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";

export function useGameSocket(gameId: string, playerId: string) {
    const [gameStarted, setGameStarted] = useState(false);
    const stompRef = useRef<Client | null>(null);

    useEffect(() => {
        if (!gameId || !playerId) return;

        const socketUrl = `ws://localhost:8080/ws?userId=${playerId}&gameId=${gameId}`;
        const client = new Client({
            brokerURL: socketUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log(`✅ WebSocket conectado para gameId=${gameId}, playerId=${playerId}`);

                client.subscribe(`/topic/game/${gameId}/start`, (message) => {
                    console.log("📥 Mensaje recibido en /start:", message.body);
                    if (message.body.trim() === "start") {
                        setGameStarted(true);
                        console.log("🎮 Partida iniciada");
                    }
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

    return { gameStarted };
}
