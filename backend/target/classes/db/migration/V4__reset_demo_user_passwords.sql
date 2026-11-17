-- Keep the local demo accounts aligned with the documented login password.
-- This is a one-time migration per database; newer accounts retain their own passwords.
CREATE EXTENSION IF NOT EXISTS pgcrypto;

UPDATE users
SET password_hash = crypt('Password@123', gen_salt('bf', 10));
