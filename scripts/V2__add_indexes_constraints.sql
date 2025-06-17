-- ================================
-- Foreign Keys & Unique Constraints
-- ================================

-- USER MODULE
ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_stores
    ADD CONSTRAINT fk_user_stores_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_user_stores_store FOREIGN KEY (store_id) REFERENCES stores (id);

-- PRODUCT MODULE
ALTER TABLE products
    ADD CONSTRAINT fk_products_category FOREIGN KEY (category_id) REFERENCES categories (id);

-- INVENTORY MODULE
ALTER TABLE inventory
    ADD CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products (id),
    ADD CONSTRAINT fk_inventory_store FOREIGN KEY (store_id) REFERENCES stores (id),
    ADD CONSTRAINT uq_inventory_product_store UNIQUE (product_id, store_id);

-- ORDER MODULE
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users (id),
    ADD CONSTRAINT fk_orders_store FOREIGN KEY (store_id) REFERENCES stores (id);

ALTER TABLE order_items
    ADD CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders (id),
    ADD CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products (id);

-- REPORTING MODULE
ALTER TABLE sales_report
    ADD CONSTRAINT fk_sales_report_store FOREIGN KEY (store_id) REFERENCES stores (id);

-- ================================
-- Indexes
-- ================================

-- USERS
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);

-- ROLES
CREATE INDEX idx_roles_name ON roles (name);

-- PRODUCTS
CREATE INDEX idx_products_name ON products (name);
CREATE INDEX idx_products_category ON products (category_id);

-- INVENTORY
CREATE INDEX idx_inventory_store ON inventory (store_id);
CREATE INDEX idx_inventory_product ON inventory (product_id);

-- ORDERS
CREATE INDEX idx_orders_user ON orders (user_id);
CREATE INDEX idx_orders_store ON orders (store_id);
CREATE INDEX idx_orders_created_at ON orders (created_at);

-- ORDER ITEMS
CREATE INDEX idx_order_items_order ON order_items (order_id);
CREATE INDEX idx_order_items_product ON order_items (product_id);

-- SALES REPORT
CREATE INDEX idx_sales_report_store_date ON sales_report (store_id, report_date);
