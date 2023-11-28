--changeset liquibase:14
INSERT INTO orders (id, user_id, total_amount, created, updated)
VALUES
    ('e110b81b-20a7-4b2a-b4e5-8ea1c0c57c03', 1, 100.00, '2023-01-01 12:00:00', '2023-01-01 12:30:00'),
    ('f210b81b-20a7-4b2a-b4e5-8ea1c0c57c04', 2, 150.50, '2023-01-02 14:00:00', '2023-01-02 14:45:00');
