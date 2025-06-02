import Image from "next/image";

export default function Navbar() {
    return (
        <nav className="flex items-center">
            <div>
                <Image
                    src="/fastwords.png"
                    alt="FastWords"
                    width={120}
                    height={0}
                    className=""
                />
                <a
                    href="https://github.com/markush0f/FastWords"
                    target="_blank"
                    rel="noopener noreferrer"
                    className="absolute top-4 right-4  px-4 py-2 rounded-full text-sm transition cursor-pointer"
                >
                    <img src="github.svg" alt="Github" width={40} />
                </a>
            </div>
        </nav>
    )
}