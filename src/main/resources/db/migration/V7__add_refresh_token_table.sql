CREATE TABLE refresh_tokens (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    refresh_token TEXT NOT NULL,
    revoked BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);