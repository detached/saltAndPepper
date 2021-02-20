CREATE TABLE recipes(
        id SERIAL PRIMARY KEY,
        recipe_id VARCHAR(36) NOT NULL UNIQUE,
        author_id VARCHAR(36) NOT NULL,
        title TEXT NOT NULL,
        category TEXT,
        cuisine TEXT,
        yields TEXT,
        ingredients TEXT,
        instructions TEXT,
        modifications TEXT,
        images TEXT
 );

 CREATE INDEX recipe_id_idx ON recipes (recipe_id);