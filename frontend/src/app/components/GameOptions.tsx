import { Search, CirclePlus, PackagePlus } from "lucide-react";

export default function GameOptions() {

    const options: {
        name: string;
        icon: React.ReactNode;
    }[] = [
            { name: "Join room!", icon: <Search /> },
            { name: "Create room!", icon: <CirclePlus /> },
            { name: "Create collection", icon: <PackagePlus /> },
        ];

    return (
        <div className="flex flex-col gap-4">
            {
                options.map((option) => (
                    <button
                        key={option.name}
                        className="w-55 h-10 bg-[#fa5d6f] text-white border-3 text-lg font-semibold cursor-pointer rounded-full hover:bg-[#fa5d6f] hover:border-[#FFD447] transition duration-300 ease-in-out flex items-center justify-between  px-5">
                        {option.name}
                        {option.icon}
                    </button>
                ))

            }
        </div>
    )
}