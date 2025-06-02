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
            </div>
        </nav>
    )
}