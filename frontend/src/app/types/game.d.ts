
export default interface Game {
    id: string;
    name: string;
    result: "win" | "lose" | "draw";
    category: string;
    opponent: Opponent;
}

interface Opponent {
    id: number;
    username: string;
}