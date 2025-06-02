'use client';
import { useState } from "react";
import { AnimatePresence, motion } from "framer-motion";
import FirstScreen from "./components/FirstScreen";
import Navbar from "./components/Navbar";
import GameOptions from "./components/GameOptions";
import LastGames from "./components/LastGames";
import PlayGame from "./components/PlayGame";
import UserStats from "./components/UserStats";

export default function Home() {
  const [firstScreenVisible, setFirstScreenVisible] = useState(true);

  return (
    <div className="">
      <AnimatePresence mode="wait">
        {firstScreenVisible ? (
          <motion.div
            key="first-screen"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
          >
            <FirstScreen setFirstScreenVisible={setFirstScreenVisible} />
          </motion.div>
        ) : (
          <motion.div
            key="main-screen"
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            className="p-6"
          >

            <div className="grid grid-cols-5 grid-rows-7 gap-4">
              <div className="col-span-5"><Navbar /></div>
              <div className="row-span-6 row-start-2"><GameOptions /></div>
              <div className="col-span-3 row-span-6 row-start-2"><PlayGame /></div>
              <div className="row-span-3 col-start-5 row-start-2"><UserStats /></div>
              <div className="row-span-3 col-start-5 row-start-5"><LastGames /></div>
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
