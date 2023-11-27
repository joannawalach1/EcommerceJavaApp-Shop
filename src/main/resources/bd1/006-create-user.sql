--liquibase formatted sql

--changeset liquibase:6

CREATE TABLE IF NOT EXISTS users
(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    email TEXT UNIQUE,
    first_name TEXT,
    last_name TEXT,
    password TEXT,
    created TIMESTAMP,
    updated TIMESTAMP
);