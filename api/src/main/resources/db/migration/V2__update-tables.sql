ALTER TABLE users
ADD COLUMN locked BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT false;

ALTER TABLE tokens
ADD COLUMN created_at DATETIME DEFAULT null,
ADD COLUMN expires_at DATETIME DEFAULT null,
ADD COLUMN validated_at DATETIME DEFAULT null;

UPDATE users SET locked = false;
UPDATE users SET enabled = true;

