DO $$
    BEGIN
        ALTER TABLE products ADD COLUMN IF NOT EXISTS unit_quantity INT;
        ALTER TABLE products ADD COLUMN IF NOT EXISTS unit_label VARCHAR(20);
        ALTER TABLE recipe_products ADD COLUMN IF NOT EXISTS amount_unit VARCHAR(20);
    END $$;
