import { useEffect, useRef, useState } from 'react';
import { Client } from '@stomp/stompjs';
import { useRouter } from 'next/navigation';

interface UseMatchmakingResult {
    gameId: string | null;
    searching: boolean;
    error: string | null;
    findMatch: (playerId: string) => void;
}

export function useMatchmaking(): UseMatchmakingResult {
    const [gameId, setGameId] = useState<string | null>(null);
    const [searching, setSearching] = useState(false);
    const [error, setError] = useState<string | null>(null);
    const [startReceived, setStartReceived] = useState(false);
    const stompRef = useRef<Client | null>(null);
    const router = useRouter();

    const findMatch = (playerId: string) => {
        setSearching(true);
        setError(null);

        const socketUrl = `ws://localhost:8080/ws?userId=${playerId}&gameId=1`;

        const client = new Client({
            brokerURL: socketUrl,
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/topic/matchmaking/${playerId}`, (message) => {
                    console.log("[Matchmaking]", message.body);
                    setGameId(message.body);
                    setSearching(false);
                });

                client.subscribe(`/topic/game/1`, (message) => {
                    console.log("[Game Start]", message.body);
                    setStartReceived(true);
                });

                client.publish({
                    destination: "/app/matchmaking",
                    body: JSON.stringify({ playerId }),
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

    useEffect(() => {
        console.log("[useMatchmaking] startReceived:", startReceived, "gameId:", gameId);
        if (startReceived && gameId) {
            router.push(`/game/${gameId}`);
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
        findMatch
    };
}
