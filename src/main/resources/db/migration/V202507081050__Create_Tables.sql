DO $$
    BEGIN
        CREATE TABLE products (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            price_in_cents INT NOT NULL
        );

        CREATE TABLE carts (
            id SERIAL PRIMARY KEY,
            total_in_cents INT NOT NULL
        );

        CREATE TABLE cart_items (
            id SERIAL PRIMARY KEY,
            cart_id INT NOT NULL,
            product_id INT NOT NULL,
            FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
            FOREIGN KEY (product_id) REFERENCES products(id)
        );


        CREATE TABLE recipes (
            id SERIAL PRIMARY KEY,
            name VARCHAR(255) NOT NULL
        );

        CREATE TABLE recipe_products (
            id SERIAL PRIMARY KEY,
            recipe_id INT NOT NULL,
            product_id INT NOT NULL,
            amount NUMERIC(10, 2) NOT NULL,
            usage_description VARCHAR(255),
            FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
            FOREIGN KEY (product_id) REFERENCES products(id),
            CONSTRAINT unique_recipe_product UNIQUE (recipe_id, product_id)
        );

    END $$;
