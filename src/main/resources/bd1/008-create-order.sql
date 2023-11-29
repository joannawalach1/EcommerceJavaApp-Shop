--liquibase formatted sql
--changeset liquibase:6
create table orders (
id uuid not null,
user_id int8,
total_amount numeric(19, 2),
created timestamp,
updated timestamp,
primary key (id),
foreign key (user_id) references users
);