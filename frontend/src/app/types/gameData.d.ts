export default interface IGameData {
    id: string;
    name: string;
    player1Id: string;
    player2Id: string;
    collectionId: string;
    gameStatus: string;
    timePerTurn: number;
}