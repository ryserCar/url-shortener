CREATE DATABASE url_shortener;

USE url_shortener;

CREATE TABLE IF NOT EXISTS urls (
    short_url VARCHAR(255) PRIMARY KEY,
    original_url VARCHAR(1000) NOT NULL
    );

INSERT INTO urls (short_url, original_url)
VALUES ('abc123','https://example.com'),
       ('xyz789','https://example.org');