ALTER TABLE if EXISTS carts ADD CONSTRAINT UK_64t7ox312pqal3p7fg9o503c2 UNIQUE (user_id);
ALTER TABLE if EXISTS cart_line_item ADD CONSTRAINT FKb6cc4bc0t8idi4nbwebxn7gt6 FOREIGN KEY (cart_id) REFERENCES carts;
ALTER TABLE if EXISTS cart_line_item ADD CONSTRAINT FK7iafh8jgn844c0ol3d2msqiga FOREIGN KEY (product_id) REFERENCES products;
ALTER TABLE if EXISTS carts ADD CONSTRAINT FKb5o626f86h46m4s7ms6ginnop FOREIGN KEY (user_id) REFERENCES users;