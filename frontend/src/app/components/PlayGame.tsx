'use client';
import React, { useState } from 'react';
import { motion } from 'framer-motion';
import Image from 'next/image';
import { Play } from 'lucide-react';
import { useMatchmaking } from '../hooks/useMatchmaking';
import { p } from 'motion/react-client';

export default function PlayGame() {
    const { gameId, searching, error, findMatch, playerId, setPlayerId } = useMatchmaking();

    const handleSearch = () => {
        if (playerId === null) {
            console.error("Player ID is null. Please enter a valid player ID.");
            return;
        }

        if (!playerId.trim()) return;
        findMatch(playerId);
    };

    return (
        <motion.div
            className="flex flex-col items-center justify-center bg-[#96b8f0] min-h-screen"
            initial={{ opacity: 1, y: 0 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -50 }}
            transition={{ duration: 0.5 }}
        >
            <Image
                src="/fast-word-play-game.png"
                alt="FastWords"
                width={500}
                height={500}
                className="object-cover -mt-20"
            />

            <input
                type="text"
                value={playerId || ''}
                onChange={(e) => setPlayerId(e.target.value)}
                placeholder="Enter your player ID"
                className="mt-6 text-2xl px-6 py-2 rounded-xl border-2 border-gray-300 focus:outline-none focus:border-blue-500"
            />

            <motion.button
                onClick={handleSearch}
                disabled={searching || !(playerId?.trim())}
                className="mt-10 w-140 h-30 bg-[#fa5d6f] text-[#ffffff] border-10 border-[#FFD447] text-5xl font-semibold cursor-pointer rounded-full flex items-center justify-center gap-2 disabled:opacity-60 disabled:cursor-not-allowed"
                animate={searching ? undefined : { y: [0, -15, 0] }}
                transition={{
                    y: {
                        duration: 1,
                        repeat: Infinity,
                        repeatType: "loop",
                        ease: "easeInOut",
                    },
                }}
                whileHover={searching ? undefined : {
                    y: 0,
                    scale: 1.05,
                    backgroundColor: "#FFD447",
                    color: "#fa5d6f",
                    transition: {
                        y: { duration: 0.3, ease: "easeOut" },
                        scale: { duration: 0.3, ease: "easeOut" },
                        backgroundColor: { duration: 0.3 },
                        color: { duration: 0.3 },
                    },
                }}
                whileTap={searching ? undefined : { scale: 0.95 }}
            >
                {searching ? "Searching..." : "Search a game!"}
                <Play width={60} height={60} className="ml-2" />
            </motion.button>

            {gameId && (
                <p className="mt-6 text-3xl text-green-800 font-bold">
                    Match found! Game ID: {gameId}
                </p>
            )}
            {error && (
                <p className="mt-4 text-red-600 text-xl font-semibold">
                    {error}
                </p>
            )}
        </motion.div>
    );
}
