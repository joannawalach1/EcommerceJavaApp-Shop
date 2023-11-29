--liquibase formatted sql

--changeset liquibase:8
create table users (
id  bigserial not null,
created timestamp,
email varchar(255),
first_name varchar(255),
last_name varchar(255),
password varchar(255),
updated timestamp,
primary key (id)
);