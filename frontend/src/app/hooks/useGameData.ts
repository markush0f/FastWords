import { useEffect, useRef, useState } from 'react';
import { Client, IMessage } from '@stomp/stompjs';

interface GameData {
    id: number;
    name: string;
    player1Id: number;
    player2Id: number;
    collectionId: number;
    gameStatus: string;
    timeRound: string;
}

interface UseGameDataResult {
    gameData: GameData | null;
    loading: boolean;
    error: string | null;
}

export function useGameData(gameId: string, userId: string): UseGameDataResult {
    const [gameData, setGameData] = useState<GameData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const stompRef = useRef<Client | null>(null);

    useEffect(() => {
        if (!gameId) return;

        const client = new Client({
            brokerURL: `ws://localhost:8080/ws?userId=${userId}&gameId=${gameId}`,
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/topic/game/${gameId}/data`, (message: IMessage) => {
                    try {
                        const data = JSON.parse(message.body);
                        setGameData(data);
                        setLoading(false);
                    } catch (e) {
                        console.error("❌ Error al parsear gameData:", e);
                        setError("Error al procesar datos de la partida");
                        setLoading(false);
                    }
                });
            },
            onStompError: (frame) => {
                console.error("STOMP error", frame);
                setError("Error de conexión WebSocket.");
                setLoading(false);
            }
        });

        client.activate();
        stompRef.current = client;

        return () => {
            client.deactivate();
        };
    }, [gameId]);

    return { gameData, loading, error };
}
