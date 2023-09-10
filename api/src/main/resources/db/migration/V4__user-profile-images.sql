ALTER TABLE users
ADD COLUMN profile_image VARCHAR(255) DEFAULT "https://i.postimg.cc/nct46HnL/avatar.png";

UPDATE users SET profile_image = "https://i.postimg.cc/nct46HnL/avatar.png";