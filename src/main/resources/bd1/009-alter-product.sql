--changeset liquibase:9
ALTER TABLE product
ADD COLUMN order_id UUID;