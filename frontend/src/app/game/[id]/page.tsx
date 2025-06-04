"use client";
import { useGameData } from '@/app/hooks/useGameData';
import { useParams, useSearchParams } from 'next/navigation';
import { useEffect } from 'react';

export default function PageGame() {
    const gameId = useParams().id as string;
    const searchParams = useSearchParams();
    const playerId = searchParams.get("playerId");
    console.log("Player ID:", playerId);
    const { gameData, loading, error } = useGameData(gameId, playerId ?? '');
    console.log("Game ID:", gameId);
    console.log("Game Data:", gameData);
    useEffect(() => {
        if (gameData) {
            console.log("🎮 Game data:", gameData);
        }
    }, [gameData]);

}
