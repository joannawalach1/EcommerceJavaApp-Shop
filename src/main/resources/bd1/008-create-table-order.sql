--changeset liquibase:8
CREATE TABLE ORDERS (
    id UUID PRIMARY KEY,
    totalAmount INT NOT NULL,
    user_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);
