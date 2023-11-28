--liquibase formatted sql

--changeset liquibase:10
alter table if exists order_line_item
add constraint FKqbouwahw8h8n7ejethegr63iu foreign key (order_id) references product;