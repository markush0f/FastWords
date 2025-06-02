'use client';
import React from 'react';
import { motion } from 'framer-motion';
import Image from 'next/image';
import { Play } from 'lucide-react';

export default function PlayGame() {
    return (
        <motion.div
            className="flex flex-col items-center justify-center bg-[#96b8f0] "
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
            <motion.button
                className="mt-10 w-140 h-30 bg-[#fa5d6f] text-[#ffffff] border-10 border-[#FFD447] text-5xl font-semibold cursor-pointer rounded-full flex items-center justify-center gap-2"
                animate={{ y: [0, -15, 0] }}
                transition={{
                    y: {
                        duration: 1,
                        repeat: Infinity,
                        repeatType: "loop",
                        ease: "easeInOut",
                    },
                }}
                whileHover={{
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
                whileTap={{ scale: 0.95 }}
            >
                Search a game!
                <Play
                    width={60}
                    height={60}
                    className="ml-2"
                />
            </motion.button>
        </motion.div>
    );
}