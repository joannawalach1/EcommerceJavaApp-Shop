--liquibase formatted sql

--changeset liquibase:2

CREATE TABLE IF NOT EXISTS product
(
    id SERIAL PRIMARY KEY,
    category_id BIGINT REFERENCES category(id),
    name TEXT,
    description TEXT,
    price NUMERIC,
    quantity INT,
    created TIMESTAMP,
    updated TIMESTAMP
);