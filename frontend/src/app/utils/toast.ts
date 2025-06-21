import toast from "react-hot-toast";

export const showPlayerTurnToast = (message: string = "It's your turn") => {
    toast.success(message, {
        style: {
            border: '1px solid #713200',
            padding: '16px',
            color: '#713200',
        },
        iconTheme: {
            primary: '#713200',
            secondary: '#FFFAEE',
        },
    });
}
export const showGameStartedToast = (message: string = "Game has started") => {
    toast.success(message, {
        style: {
            border: '1px solid #28a745',
            padding: '16px',
            color: '#155724',
        },
        iconTheme: {
            primary: '#155724',
            secondary: '#d4edda',
        },
    });
}