CREATE TABLE url
(
    id           UUID          NOT NULL,
    short_code   VARCHAR(255)  NOT NULL,
    original_url VARCHAR(2048) NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_url PRIMARY KEY (id)
);

ALTER TABLE url
    ADD CONSTRAINT uc_url_shortcode UNIQUE (short_code);