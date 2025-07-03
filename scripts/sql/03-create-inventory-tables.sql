-- ================================
-- Schema: inventory
-- ================================

CREATE SCHEMA IF NOT EXISTS inventory;

-- ================================
-- Table: inventory
-- ================================
CREATE TABLE IF NOT EXISTS inventory.inventory (
    id              VARCHAR(36) PRIMARY KEY,
    product_id      VARCHAR(36) NOT NULL,
    store_id        VARCHAR(36) NOT NULL,
    quantity        INTEGER NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_counted_at TIMESTAMP,
    CONSTRAINT fk_inventory_product FOREIGN KEY (product_id)
        REFERENCES products.products (id),
    CONSTRAINT fk_inventory_store FOREIGN KEY (store_id)
        REFERENCES users.stores (id),
    UNIQUE (product_id, store_id)
);

-- ========================================
-- Indexes
-- ========================================
CREATE INDEX IF NOT EXISTS idx_inventory_product ON inventory.inventory (product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_store ON inventory.inventory (store_id);
CREATE INDEX IF NOT EXISTS idx_inventory_product_store ON inventory.inventory (product_id, store_id);
