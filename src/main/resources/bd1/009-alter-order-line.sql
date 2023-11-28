--liquibase formatted sql

--changeset liquibase:9
alter table if exists order_line_item
add constraint FKt3tyis4keikugdsqgjbgb7avr foreign key (product_id) references orders;