"use client";
import { useGameData } from '@/app/hooks/useGameData';
import { useMatchmaking } from '@/app/hooks/useMatchmaking';
import { useParams } from 'next/navigation';
import { useEffect } from 'react';

export default function PageGame() {
    const gameId = useParams().id as string;
    const { playerId } = useMatchmaking();
    const { gameData, loading, error } = useGameData(gameId, playerId || '');
    console.log("Game ID:", gameId);
    console.log("Game Data:", gameData);
    useEffect(() => {
        if (gameData) {
            console.log("🎮 Game data:", gameData);
        }
    }, [gameData]);

}
