ALTER TABLE users
ADD COLUMN mfa_enabled BOOLEAN DEFAULT false,
ADD COLUMN secret VARCHAR(255);

UPDATE users SET mfa_enabled = false;