INSERT INTO users (email, first_name, last_name, password, created, updated)
VALUES
    ('user1@example.com', 'John', 'Doe', 'password1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('user2@example.com', 'Jane', 'Smith', 'password2', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    ('admin1@example.com', 'Admin', 'Adminowski', 'adminpass1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);