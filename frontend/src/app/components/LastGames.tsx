import Game from "../types/game";

export default function LastGames() {

    const games: Game[] = [
        {
            id: "1",
            name: "Game 1",
            result: "win",
            category: "Category 1",
            opponent: {
                id: 1,
                username: "Opponent 1",
            },
        },
        {
            id: "2",
            name: "Game 2",
            result: "lose",
            category: "Category 2",
            opponent: {
                id: 1,
                username: "Opponent 1",
            },
        },
        {
            id: "3",
            name: "Game 3",
            result: "draw",
            category: "Category 3",
            opponent: {
                id: 1,
                username: "Opponent 1",
            },
        }
    ];

    return (
        <div className="flex flex-col gap-6">
            <h2 className="text-3xl font-extrabold text-gray-800">Last Games</h2>
            <div>
                {games.length === 0 ? (
                    <p className="text-gray-600 text-lg">No games played yet.</p>
                ) : (
                    <ul className="space-y-3">
                        {games.map((game) => (
                            <li
                                key={game.id}
                                className={`cursor-pointer flex items-center justify-between px-4 py-3 rounded-lg shadow-md transition-all duration-200 hover:scale-[1.01] ${game.result === "win"
                                    ? "bg-green-400/90"
                                    : game.result === "lose"
                                        ? "bg-red-400/90"
                                        : "bg-gray-300/90"
                                    }`}
                            >
                                <div className="flex flex-col items-center gap-1 text-white font-semibold">
                                    <span className="uppercase tracking-wide">{game.category}</span>
                                </div>

                                <span className="font-extrabold text-2xl text-white drop-shadow-sm">VS</span>

                                <div>
                                    <span className="text-white font-semibold text-lg">
                                        {game.opponent.username}
                                    </span>
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>

    );
}