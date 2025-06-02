export default function UserStats() {
    const stats = {
        gamesPlayed: 0,
        gamesWon: 0,
        gamesLost: 0,
        winRate: 0,
        averageScore: 0,
        highestScore: 0,
    }
    return (
        <div className="flex flex-col items-center justify-center h-full">
            <h2 className="text-3xl font-extrabold mb-6 text-white">User Statistics</h2>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full max-w-md">
                {Object.entries(stats).map(([key, value]) => (
                    <div
                        key={key}
                        className="bg-white rounded-lg shadow p-4 flex flex-col items-center"
                    >
                        <span className="text-gray-500 capitalize">
                            {key.replace(/([A-Z])/g, ' $1')}
                        </span>
                        <span className="text-xl font-bold text-blue-600">{value}</span>
                    </div>
                ))}
            </div>

        </div>
    );
}