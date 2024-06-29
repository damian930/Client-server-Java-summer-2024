-- Create Category table

CREATE TABLE IF NOT EXISTS Category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

-- Create Product table
CREATE TABLE IF NOT EXISTS Product (
    id SERIAL PRIMARY KEY,
    UPC VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) UNIQUE NOT NULL,
    sellingPrice NUMERIC(10, 2) CHECK (sellingPrice >= 0.0) NOT NULL,
    quantity INT CHECK (quantity >= 0) NOT NULL,
    characteristics TEXT,
    category_id INT REFERENCES Category(id) ON DELETE CASCADE
);

DROP TABLE Category;
DROP TABLE Product;

INSERT INTO Category (name)
VALUES ('Electronics'),
       ('Clothing'),
       ('Books');

INSERT INTO Product (UPC, name, sellingPrice, quantity, characteristics, category_id)
VALUES ('1', 'Laptop', 1200.0, 10, 'High-performance laptop', 1),
       ('2', 'T-shirt', 19.99, 50, '100% cotton', 2),
       ('3', 'Java Programming Book', 49.99, 30, 'Complete guide to Java programming', 3);