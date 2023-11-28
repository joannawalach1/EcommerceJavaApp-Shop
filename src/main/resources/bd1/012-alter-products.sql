--liquibase formatted sql

--changeset liquibase:12
alter table if exists product
add constraint FK1cf90etcu98x1e6n9aks3tel3 foreign key (category_id) references category;