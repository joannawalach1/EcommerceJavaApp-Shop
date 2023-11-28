--liquibase formatted sql

--changeset liquibase:7
create table order_line_item
(
order_id int8 not null,
product_id uuid not null,
primary key (order_id, product_id)
);