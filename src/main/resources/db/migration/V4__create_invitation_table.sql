CREATE TABLE invitations(
        id SERIAL PRIMARY KEY,
        user_id VARCHAR(36) NOT NULL,
        invitation_code TEXT NOT NULL,
        created_on TIMESTAMP WITH TIME ZONE NOT NULL
);