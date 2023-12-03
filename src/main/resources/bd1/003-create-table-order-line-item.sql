CREATE TABLE order_line_item (
id INT PRIMARY KEY NOT NULL,
order_id UUID,
product_id INT,
quantity INT
);