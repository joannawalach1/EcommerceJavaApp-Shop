--liquibase formatted sql

--changeset liquibase:1

CREATE TABLE IF NOT EXISTS category
(
    id SERIAL PRIMARY KEY not null,
    name TEXT,
    created TIMESTAMP,
    updated TIMESTAMP
);