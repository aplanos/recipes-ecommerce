WITH products_data AS (
    INSERT INTO products (name, price_in_cents, unit_quantity, unit_label)
        VALUES
            ('Flour', 350, 1000, 'G'),
            ('Sugar', 280, 1000, 'G'),
            ('Milk', 450, 1000, 'ML'),
            ('Butter', 600, 200, 'G'),
            ('Egg', 150, 1, 'PCS')
        RETURNING id, name
),
     carts_data AS (
         INSERT INTO carts (total_in_cents)
             VALUES (0), (0)
             RETURNING id
     ),
     recipes_data AS (
         INSERT INTO recipes (name)
             VALUES ('Pancake'), ('Cake')
             RETURNING id, name
     ),
     cart_items_insert AS (
         INSERT INTO cart_items (cart_id, product_id, quantity, source_type)
             SELECT c1.id, p1.id, 2, 'PRODUCT'
             FROM (SELECT MIN(id) AS id FROM carts_data) c1
                      JOIN products_data p1 ON p1.name = 'Flour'

             UNION ALL

             SELECT c1.id, p2.id, 1, 'PRODUCT'
             FROM (SELECT MIN(id) AS id FROM carts_data) c1
                      JOIN products_data p2 ON p2.name = 'Sugar'

             UNION ALL

             SELECT c2.id, p3.id, 3, 'PRODUCT'
             FROM (SELECT MAX(id) AS id FROM carts_data) c2
                      JOIN products_data p3 ON p3.name = 'Milk'
     ),
     recipe_products_insert AS (
         INSERT INTO recipe_products (recipe_id, product_id, amount, usage_description, amount_unit)
             SELECT r.id, p.id, 200, 'for batter', 'G'
             FROM recipes_data r JOIN products_data p ON r.name = 'Pancake' AND p.name = 'Flour'

             UNION ALL

             SELECT r.id, p.id, 300, 'mix with flour', 'ML'
             FROM recipes_data r JOIN products_data p ON r.name = 'Pancake' AND p.name = 'Milk'

             UNION ALL

             SELECT r.id, p.id, 2, 'for mixture', 'PCS'
             FROM recipes_data r JOIN products_data p ON r.name = 'Pancake' AND p.name = 'Egg'

             UNION ALL

             SELECT r.id, p.id, 300, 'base of cake', 'G'
             FROM recipes_data r JOIN products_data p ON r.name = 'Cake' AND p.name = 'Flour'

             UNION ALL

             SELECT r.id, p.id, 200, 'sweetness', 'G'
             FROM recipes_data r JOIN products_data p ON r.name = 'Cake' AND p.name = 'Sugar'

             UNION ALL

             SELECT r.id, p.id, 100, 'moisture', 'G'
             FROM recipes_data r JOIN products_data p ON r.name = 'Cake' AND p.name = 'Butter'
     )

-- Final dummy SELECT to close the CTE block (required in PostgreSQL)
SELECT 1;
