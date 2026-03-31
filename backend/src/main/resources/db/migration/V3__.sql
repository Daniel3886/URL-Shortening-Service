CREATE TABLE url_clicks
(
    id          UUID          NOT NULL,
    short_code  VARCHAR(255)  NOT NULL,
    ip_address  VARCHAR(45),
    clicked_at  TIMESTAMP     NOT NULL,
    url_id      UUID          NOT NULL,
    CONSTRAINT  pk_url_clicks PRIMARY KEY (id)
);

ALTER TABLE url_clicks
    ADD CONSTRAINT FK_URL_CLICKS_ON_URL FOREIGN KEY (url_id) REFERENCES url (id);