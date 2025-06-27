-- ========================================
-- Tables for Orders Microservice (Schema: orders)
-- ========================================

CREATE TABLE IF NOT EXISTS orders.orders (
    id           VARCHAR(36) PRIMARY KEY,
    user_id      VARCHAR(36) NOT NULL,
    store_id     VARCHAR(36) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    status       VARCHAR(50) NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users.users (id),
    CONSTRAINT fk_orders_store FOREIGN KEY (store_id) REFERENCES users.stores (id)
);

CREATE TABLE IF NOT EXISTS orders.order_items (
    id         VARCHAR(36) PRIMARY KEY,
    order_id   VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    quantity   INTEGER NOT NULL,
    price      DECIMAL(12, 2) NOT NULL,
    total      DECIMAL(12, 2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders.orders (id),
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products.products (id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders.orders (user_id);
CREATE INDEX IF NOT EXISTS idx_orders_store ON orders.orders (store_id);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders.orders (created_at);

CREATE INDEX IF NOT EXISTS idx_order_items_order ON orders.order_items (order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON orders.order_items (product_id);
