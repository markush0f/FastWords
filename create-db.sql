-- Tabla de usuarios
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Colecciones de palabras (categorías como "frutas", "animales", etc.)
CREATE TABLE collections (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Palabras pertenecientes a una colección
CREATE TABLE words (
    id SERIAL PRIMARY KEY,
    word VARCHAR(100) NOT NULL,
    collection_id INT NOT NULL,
    FOREIGN KEY (collection_id) REFERENCES collections(id),
    UNIQUE (word, collection_id) -- evita palabras duplicadas en la misma colección
);

-- Partidas entre dos jugadores
CREATE TABLE games (
    id SERIAL PRIMARY KEY,
    player1_id INT NOT NULL,
    player2_id INT NOT NULL,
    collection_id INT NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'ACTIVE', 'FINISHED')),
    winner_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (player1_id) REFERENCES users(id),
    FOREIGN KEY (player2_id) REFERENCES users(id),
    FOREIGN KEY (collection_id) REFERENCES collections(id),
    FOREIGN KEY (winner_id) REFERENCES users(id)
);

-- Turnos de los jugadores en la partida
CREATE TABLE turns (
    id SERIAL PRIMARY KEY,
    game_id INT NOT NULL,
    user_id INT NOT NULL,
    word VARCHAR(100) NOT NULL,
    is_valid BOOLEAN NOT NULL,
    response_time DECIMAL(5,2) NOT NULL, -- en segundos, ej: 3.45
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Palabras ya usadas en una partida (para evitar repeticiones)
CREATE TABLE used_words (
    id SERIAL PRIMARY KEY,
    game_id INT NOT NULL,
    word_id INT NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(id),
    FOREIGN KEY (word_id) REFERENCES words(id),
    UNIQUE (game_id, word_id) -- asegura que no se repite palabra en misma partida
);
