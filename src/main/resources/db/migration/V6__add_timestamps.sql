ALTER TABLE recipes ADD COLUMN created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now();
ALTER TABLE recipes ADD COLUMN modified_at TIMESTAMP WITH TIME ZONE;