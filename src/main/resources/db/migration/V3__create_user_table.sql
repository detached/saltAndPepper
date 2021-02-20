CREATE TABLE users(
        id SERIAL PRIMARY KEY,
        user_id VARCHAR(36) NOT NULL UNIQUE,
        username VARCHAR(30) NOT NULL UNIQUE,
        password TEXT NOT NULL,
        role TEXT NOT NULL
);

CREATE INDEX username_idx ON users (username);