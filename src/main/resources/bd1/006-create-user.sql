--liquibase formatted sql

--changeset liquibase:6

CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY,
    email TEXT UNIQUE,
    first_name TEXT,
    last_name TEXT,
    password TEXT,
    created TIMESTAMP,
    updated TIMESTAMP
);