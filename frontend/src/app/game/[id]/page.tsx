"use client";
import { useGameData } from '@/app/hooks/useGameData';
import { useParams, useSearchParams } from 'next/navigation';
import { useEffect } from 'react';

export default function PageGame() {
    const { id } = useParams();
    const gameId = id as string;
    const searchParams = useSearchParams();
    const playerId = searchParams.get("playerId") ?? '';

    const { gameData, loading, error } = useGameData(gameId, playerId);

    useEffect(() => {
        if (gameData) {
            console.log("🎮 Game data:", gameData);
        }
    }, [gameData]);

    if (loading) return <p>🔄 Cargando juego...</p>;
    if (error) return <p>❌ Error: {error}</p>;

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold">🎮 Juego #{gameData.id}</h1>
            <p>Jugador 1: {gameData.player1Id}</p>
            <p>Jugador 2: {gameData.player2Id}</p>
            <p>Tiempo por turno: {gameData.timePerTurn} segundos</p>
            <p>Colección: {gameData.collectionId}</p>
        </div>
    );
}
 