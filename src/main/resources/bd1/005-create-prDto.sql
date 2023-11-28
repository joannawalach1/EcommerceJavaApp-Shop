--liquibase formatted sql

--changeset liquibase:5
CREATE TABLE IF NOT EXISTS product_dto (
    id SERIAL PRIMARY KEY,
    category_type VARCHAR(255),
    name VARCHAR(255),
    description VARCHAR(255),
    price NUMERIC,
    quantity INT,
    created TIMESTAMP,
    updated TIMESTAMP
);
