"use client";
import { useGameData } from '@/app/hooks/useGameData';
import { useGameSocket } from '@/app/hooks/useGameSocket';
import { useParams, useSearchParams } from 'next/navigation';
import { useState } from 'react';

export default function PageGame() {
    const { id } = useParams();
    const gameId = id as string;
    const searchParams = useSearchParams();
    const playerId = searchParams.get("playerId") ?? '';

    const { gameData, loading, error } = useGameData(gameId, playerId);
    const { gameStarted, lastWord, sendTurn, currentTurnPlayerId } = useGameSocket(gameId, playerId);
    const [inputWord, setInputWord] = useState("");

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (inputWord.trim()) {
            sendTurn(inputWord.trim().toLowerCase());
            setInputWord("");
        }
    };

    if (loading) return <p>🔄 Cargando juego...</p>;
    if (error) return <p>❌ Error: {error}</p>;
    if (!gameData) return <p>❗ No se encontraron datos del juego.</p>;

    const isMyTurn = currentTurnPlayerId === playerId;

    return (
        <div className="p-4">
            <h1 className="text-2xl font-bold">🎮 Juego #{gameData.id}</h1>
            <p>Jugador 1: {gameData.player1Id}</p>
            <p>Jugador 2: {gameData.player2Id}</p>
            <p>Colección: {gameData.collectionId}</p>
            <p>⏳ Estado: {gameStarted ? "En curso" : "Esperando inicio..."}</p>
            <p>📦 Última palabra jugada: {lastWord ?? "Ninguna"}</p>
            <p>🧠 Turno actual: {
                currentTurnPlayerId
                    ? isMyTurn
                        ? "¡Es tu turno!"
                        : "Esperando al otro jugador..."
                    : "Aún no asignado"
            }</p>

            {gameStarted && isMyTurn && (
                <form onSubmit={handleSubmit} className="mt-4">
                    <input
                        type="text"
                        value={inputWord}
                        onChange={(e) => setInputWord(e.target.value)}
                        className="border p-2 mr-2"
                        placeholder="Escribe una palabra..."
                    />
                    <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded">
                        Enviar
                    </button>
                </form>
            )}

            {gameStarted && !isMyTurn && (
                <p className="mt-4 text-gray-600">⏳ Esperando el turno del otro jugador...</p>
            )}
        </div>
    );
}
