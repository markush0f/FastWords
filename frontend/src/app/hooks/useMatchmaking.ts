import { useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { useRouter } from 'next/navigation';

interface UseMatchmakingResult {
    gameId: string | null;
    searching: boolean;
    error: string | null;
    findMatch: (playerId: string) => void;
    playerId: string | null;
    setPlayerId: (id: string | null) => void;
}

export function useMatchmaking(): UseMatchmakingResult {
    const [playerId, setPlayerId] = useState<string | null>(null);
    const [gameId, setGameId] = useState<string | null>(null);
    const [searching, setSearching] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [startReceived, setStartReceived] = useState(false);
    const stompRef = useRef<Client | null>(null);
    const router = useRouter();
    const playerIdRef = useRef<string | null>(null); // ✅ siempre actualizado

    const setPlayerIdSafe = (id: string | null) => {
        setPlayerId(id);
        playerIdRef.current = id;
    };

    const findMatch = (id: string) => {
        setSearching(true);
        setError(null);
        setPlayerIdSafe(id); // ✅ actualizamos el ref

        const socketUrl = `ws://localhost:8080/ws?userId=${id}`; // ⚠️ solo userId, sin gameId aún

        const client = new Client({
            brokerURL: socketUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/topic/matchmaking/${id}`, (message) => {
                    const newGameId = message.body;
                    console.log("[Matchmaking] Match found! Game ID:", newGameId);
                    setGameId(newGameId);
                    setSearching(false);

                    client.deactivate().then(() => {
                        connectToGameWebSocket(id, newGameId);
                    });
                });

                client.publish({
                    destination: "/app/matchmaking",
                    body: JSON.stringify({ playerId: id, collectionId: 1 }),
                });
            },
            onStompError: (frame) => {
                console.error("STOMP error", frame);
                setError("WebSocket connection error.");
                setSearching(false);
            },
        });

        client.activate();
        stompRef.current = client;
    };

    const connectToGameWebSocket = (userId: string, gameId: string) => {
        const socketUrl = `ws://localhost:8080/ws?userId=${userId}&gameId=${gameId}`;

        const client = new Client({
            brokerURL: socketUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                console.log("🧩 Connected to game WebSocket:", socketUrl);

                client.subscribe(`/topic/game/${gameId}/data`, (message) => {
                    const gameData = JSON.parse(message.body);
                    console.log("[Game Start]", gameData);
                    setStartReceived(true);
                });

                client.publish({
                    destination: "/app/game/data/request",
                    body: JSON.stringify({ playerId: userId, gameId: gameId }),
                });
            },
            onStompError: (frame) => {
                console.error("STOMP error in game connection", frame);
                setError("WebSocket game connection error.");
            },
        });

        client.activate();
        stompRef.current = client;
    };



    useEffect(() => {
        console.log("[useMatchmaking] startReceived:", startReceived, "gameId:", gameId);
        if (startReceived && gameId) {
            router.push(`/game/${gameId}?playerId=${playerIdRef.current}`);
            setStartReceived(false);
        }
    }, [startReceived, gameId, router]);

    useEffect(() => {
        const handleBeforeUnload = () => {
            stompRef.current?.deactivate();
        };

        window.addEventListener("beforeunload", handleBeforeUnload);
        return () => {
            window.removeEventListener("beforeunload", handleBeforeUnload);
            stompRef.current?.deactivate();
        };
    }, []);

    return {
        gameId,
        searching,
        error,
        findMatch,
        playerId,
        setPlayerId: setPlayerIdSafe,
    };
}
