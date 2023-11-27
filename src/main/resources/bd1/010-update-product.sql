--changeset liquibase:10
ALTER TABLE product
ADD CONSTRAINT fk_order
FOREIGN KEY (order_id)
REFERENCES ORDERS(id);