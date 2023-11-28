--liquibase formatted sql

--changeset liquibase:11
alter table if exists orders
add constraint FK32ql8ubntj5uh44ph9659tiih foreign key (user_id) references users;