-- ================================
-- Schema: orders
-- ================================

CREATE SCHEMA IF NOT EXISTS orders;

-- ========================================
-- Table: orders
-- ========================================
CREATE TABLE IF NOT EXISTS orders.orders (
    id             VARCHAR(36) PRIMARY KEY,
    user_id        VARCHAR(36) NOT NULL,
    store_id       VARCHAR(36) NOT NULL,
    total_amount   DECIMAL(12, 2) NOT NULL,
    status         VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(50) NOT NULL DEFAULT 'CASH',
    order_type     VARCHAR(50) DEFAULT 'SALE',
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users.users (id),
    CONSTRAINT fk_orders_store FOREIGN KEY (store_id) REFERENCES users.stores (id)
);

-- ========================================
-- Table: order items
-- ========================================
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

-- ========================================
-- Table: closures
-- ========================================
CREATE TABLE IF NOT EXISTS orders.closures (
    id               VARCHAR(36) PRIMARY KEY,
    store_id         VARCHAR(36) NOT NULL,
    user_id          VARCHAR(36) NOT NULL,
    closure_date     DATE NOT NULL,
    start_datetime   TIMESTAMP NOT NULL,
    end_datetime     TIMESTAMP NOT NULL,
    total_sales      DECIMAL(12, 2) NOT NULL,
    total_cash       DECIMAL(12, 2) NOT NULL,
    total_card       DECIMAL(12, 2) NOT NULL,
    total_other      DECIMAL(12, 2) NOT NULL,
    total_orders     INTEGER NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_closures_store FOREIGN KEY (store_id) REFERENCES users.stores(id),
    CONSTRAINT fk_closures_user FOREIGN KEY (user_id) REFERENCES users.users(id),
    UNIQUE(store_id, closure_date)
);

-- ========================================
-- Indexes
-- ========================================
CREATE INDEX IF NOT EXISTS idx_orders_user ON orders.orders (user_id);
CREATE INDEX IF NOT EXISTS idx_orders_store ON orders.orders (store_id);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders.orders (created_at);

CREATE INDEX IF NOT EXISTS idx_order_items_order ON orders.order_items (order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_product ON orders.order_items (product_id);

CREATE INDEX IF NOT EXISTS idx_closures_store_date ON orders.closures (store_id, closure_date);
CREATE INDEX IF NOT EXISTS idx_closures_status ON orders.closures (status);
