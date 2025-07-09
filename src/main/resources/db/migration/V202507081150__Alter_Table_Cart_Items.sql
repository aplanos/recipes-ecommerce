DO $$
    BEGIN
        ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS source_type VARCHAR(20);
        ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS source_id INT;
        ALTER TABLE cart_items ADD COLUMN IF NOT EXISTS quantity INT;

        UPDATE cart_items SET source_type = 'PRODUCT', source_id = NULL WHERE source_type IS NULL;

        ALTER TABLE cart_items ALTER COLUMN source_type SET DEFAULT 'PRODUCT';
        ALTER TABLE cart_items ALTER COLUMN source_type SET NOT NULL;

        IF NOT EXISTS (
            SELECT 1 FROM information_schema.table_constraints
            WHERE table_name = 'cart_items'
              AND constraint_type = 'UNIQUE'
              AND constraint_name = 'uk_cart_items_context'
        ) THEN
            EXECUTE 'ALTER TABLE cart_items
                 ADD CONSTRAINT uk_cart_items_context
                 UNIQUE (cart_id, product_id, source_type, source_id)';
        END IF;
    END $$;
