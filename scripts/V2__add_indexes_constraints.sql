-- ================================
-- Indexes
-- ================================
-- USERS
CREATE INDEX idx_users_username
    ON users (username);

CREATE INDEX idx_users_email
    ON users (email);

-- ROLES
CREATE INDEX idx_roles_name
    ON roles (NAME);

-- PRODUCTS
CREATE INDEX idx_products_name
    ON products (NAME);

CREATE INDEX idx_products_category
    ON products (category_id);

-- INVENTORY
CREATE INDEX idx_inventory_store
    ON inventory (store_id);

CREATE INDEX idx_inventory_product
    ON inventory (product_id);

-- ORDERS
CREATE INDEX idx_orders_user
    ON orders (user_id);

CREATE INDEX idx_orders_store
    ON orders (store_id);

CREATE INDEX idx_orders_created_at
    ON orders (created_at);

-- ORDER ITEMS
CREATE INDEX idx_order_items_order
    ON order_items (order_id);

CREATE INDEX idx_order_items_product
    ON order_items (product_id);

-- SALES REPORT
CREATE INDEX idx_sales_report_store_date
    ON sales_report (store_id, report_date);