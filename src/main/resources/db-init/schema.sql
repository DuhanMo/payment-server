CREATE TABLE samples
(
    id    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE articles
(
    id         BIGINT AUTO_INCREMENT PRIMARY  KEY,
    title      VARCHAR(200),
    body       VARCHAR(2000),
    author_id  BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);