CREATE TABLE users (
    user_id bigserial PRIMARY KEY NOT NULL,
    email TEXT,
    first_name TEXT,
    last_name TEXT,
    password TEXT,
    created TIMESTAMP,
    updated TIMESTAMP
);