import { useEffect, useState } from "react";
import IGameData from "../types/gameData";

export function useGameData(gameId: string, playerId: string) {
    const [gameData, setGameData] = useState<IGameData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        if (!gameId) return;

        const fetchGameData = async () => {
            try {
                setLoading(true);
                const res = await fetch(`http://localhost:8080/api/v1/game/${gameId}`);
                if (!res.ok) throw new Error("Game not found");
                const data = await res.json();
                setGameData(data);
            } catch (err: any) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchGameData();
    }, [gameId]);

    return { gameData, loading, error };
}
