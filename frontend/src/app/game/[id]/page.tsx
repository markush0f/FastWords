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

    if (loading) return <p className="p-4">🔄 Cargando juego...</p>;
    if (error) return <p className="p-4">❌ Error: {error}</p>;
    if (!gameData) return <p className="p-4">❗ No se encontraron datos del juego.</p>;

    const isMyTurn = currentTurnPlayerId === playerId;

    return (
        <div className="p-4 bg-gray-100 rounded-lg shadow-md">
            <h1 className="text-2xl font-bold mb-4">🎮 Juego #{gameData.id}</h1>
            <div className="mb-4">
                <p><span className="font-semibold">Jugador 1:</span> {gameData.player1Id}</p>
                <p><span className="font-semibold">Jugador 2:</span> {gameData.player2Id}</p>
                <p><span className="font-semibold">Colección:</span> {gameData.collectionId}</p>
                <p><span className="font-semibold">⏳ Estado:</span> {gameStarted ? "En curso" : "Esperando inicio..."}</p>
                <p><span className="font-semibold">📦 Última palabra jugada:</span> {lastWord ?? "Ninguna"}</p>
                <p><span className="font-semibold">🧠 Turno actual:</span> {
                    currentTurnPlayerId
                        ? isMyTurn
                            ? "¡Es tu turno!"
                            : "Esperando al otro jugador..."
                        : "Aún no asignado"
                }</p>
            </div>

            {gameStarted && isMyTurn && (
                <form onSubmit={handleSubmit} className="mt-4">
                    <input
                        type="text"
                        value={inputWord}
                        onChange={(e) => setInputWord(e.target.value)}
                        className="border border-gray-300 p-2 mr-2 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-400"
                        placeholder="Escribe una palabra..."
                    />
                    <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-400">
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
