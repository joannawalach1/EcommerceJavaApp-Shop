create table carts (
id BIGSERIAL PRIMARY KEY NOT NULL,
created timestamp,
total_price numeric(15, 2),
updated timestamp,
user_id INT NOT NULL
);
