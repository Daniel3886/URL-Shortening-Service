ALTER TABLE url
    ADD COLUMN updated_at TIMESTAMP;

UPDATE url
SET updated_at = now()
WHERE updated_at IS NULL;