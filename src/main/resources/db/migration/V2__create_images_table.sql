CREATE TABLE images(
        id SERIAL PRIMARY KEY,
        image_id VARCHAR(36) NOT NULL UNIQUE,
        data BYTEA NOT NULL,
        thumbnail BYTEA NOT NULL
 );

 CREATE INDEX image_id_idx ON images (image_id);