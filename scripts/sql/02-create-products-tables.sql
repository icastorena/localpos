-- ================================
-- Schema: products
-- ================================

CREATE SCHEMA IF NOT EXISTS products;

-- ================================
-- Table: categories
-- ================================

CREATE TABLE IF NOT EXISTS products.categories (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(150) NOT NULL UNIQUE,
    description TEXT
);

-- ================================
-- Table: products
-- ================================

CREATE TABLE IF NOT EXISTS products.products (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    price       DECIMAL(12, 2) NOT NULL,
    category_id VARCHAR(36),
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_products_category FOREIGN KEY (category_id)
        REFERENCES products.categories (id)
);

-- ========================================
-- Indexes
-- ========================================
CREATE INDEX IF NOT EXISTS idx_products_name ON products.products (name);
CREATE INDEX IF NOT EXISTS idx_products_category ON products.products (category_id);
