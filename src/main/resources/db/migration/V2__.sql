ALTER TABLE url
    ADD url VARCHAR(2048);

UPDATE url
SET url = original_url,
    updated_at = now()
where url is null;

ALTER TABLE url
    DROP
        COLUMN original_url;
