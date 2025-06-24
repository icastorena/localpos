-- ================================
-- Seed Data for POS System
-- ================================
-- ROLES

INSERT INTO ROLES (name,
                   description)
VALUES ('ADMIN', 'Administrator with full permissions'),
       ('CASHIER', 'Cashier user with limited permissions'),
       ('MANAGER', 'Store manager with intermediate permissions'),
       ('OWNER', 'Owner with highest level permissions');

-- USERS

INSERT INTO users (username, password, email, created_at, updated_at)
VALUES ('admin', '$2a$10$GRc8bnRWVpYf0419z1x3HeadTolLbfKh4x2uZr3PY/LrWdtSfIB4.', 'admin@localpos.com', NOW(), NOW()),
       ('cashier1', '$2a$10$3VWMK5xf5Wv6CmOQi/Xy5uxbGWcGL.0wGzNTGyJEdorak1SQg1dKG', 'cashier1@localpos.com', NOW(), NOW()),
       ('manager1', '$2a$10$rBIsEs70Ao/kus6psJNGhunBn5GTxJyk/8O8M161zfypBU1Ih5MlO', 'manager1@localpos.com', NOW(), NOW()),
       ('cashier2', '$2a$10$R1wZ1MZR7Q.TdUuRf068T.sfZWMuyKYXbeqECULLNRNauRm60Yj66', 'cashier2@localpos.com', NOW(), NOW()),
       ('owner1', '$2a$12$t96OUJwl8LI43tU33B2Lu.IlUXIK9alTTQDf0.Y2ANXTrKqQeVR6K', 'owner1@localpos.com', NOW(), NOW());

-- USER ROLES

INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM ROLES WHERE name = 'ADMIN')),
       ((SELECT id FROM users WHERE username = 'cashier1'), (SELECT id FROM ROLES WHERE name = 'CASHIER')),
       ((SELECT id FROM users WHERE username = 'cashier2'), (SELECT id FROM ROLES WHERE name = 'CASHIER')),
       ((SELECT id FROM users WHERE username = 'manager1'), (SELECT id FROM ROLES WHERE name = 'MANAGER')),
       ((SELECT id FROM users WHERE username = 'owner1'), (SELECT id FROM ROLES WHERE name = 'OWNER'));

-- STORES

INSERT INTO stores (code, name, address, created_at, updated_at)
VALUES ('MAIN', 'Main Branch', '123 Main St, Cityville', NOW(), NOW()),
       ('WEST', 'West Side Outlet', '450 Market St, West District', NOW(), NOW());

-- USER STORES

INSERT INTO user_stores (user_id, store_id)
VALUES ((SELECT id FROM users WHERE username = 'admin'), (SELECT id FROM stores WHERE name = 'Main Branch')),
       ((SELECT id FROM users WHERE username = 'cashier1'), (SELECT id FROM stores WHERE name = 'Main Branch')),
       ((SELECT id FROM users WHERE username = 'manager1'), (SELECT id FROM stores WHERE name = 'West Side Outlet')),
       ((SELECT id FROM users WHERE username = 'cashier2'), (SELECT id FROM stores WHERE name = 'West Side Outlet')),
       ((SELECT id FROM users WHERE username = 'owner1'), (SELECT id FROM stores WHERE name = 'Main Branch'));

-- CATEGORIES

INSERT INTO categories (name, description)
VALUES ('Beverages', 'Drinks and refreshments'),
       ('Food', 'Food items and snacks'),
       ('Desserts', 'Sweet treats and desserts');

-- PRODUCTS

INSERT INTO products (name, description, price, category_id, created_at, updated_at)
VALUES ('Coffee', 'Fresh brewed coffee', 2.50, (SELECT id FROM categories WHERE name = 'Beverages'), NOW(), NOW()),
       ('Tea', 'Organic green tea', 2.00, (SELECT id FROM categories WHERE name = 'Beverages'), NOW(), NOW()),
       ('Sandwich', 'Ham and cheese sandwich', 5.00, (SELECT id FROM categories WHERE name = 'Food'), NOW(), NOW()),
       ('Salad', 'Fresh garden salad', 4.50, (SELECT id FROM categories WHERE name = 'Food'), NOW(), NOW()),
       ('Cake Slice', 'Chocolate cake slice', 3.50, (SELECT id FROM categories WHERE name = 'Desserts'), NOW(), NOW());

-- INVENTORY

INSERT INTO inventory (product_id, store_id, quantity, last_updated)
VALUES ((SELECT id FROM products WHERE name = 'Coffee'), (SELECT id FROM stores WHERE name = 'Main Branch'), 100, NOW()),
       ((SELECT id FROM products WHERE name = 'Tea'), (SELECT id FROM stores WHERE name = 'Main Branch'), 50, NOW()),
       ((SELECT id FROM products WHERE name = 'Sandwich'), (SELECT id FROM stores WHERE name = 'Main Branch'), 30, NOW()),
       ((SELECT id FROM products WHERE name = 'Salad'), (SELECT id FROM stores WHERE name = 'Main Branch'), 20, NOW()),
       ((SELECT id FROM products WHERE name = 'Cake Slice'), (SELECT id FROM stores WHERE name = 'Main Branch'), 15, NOW()),
       ((SELECT id FROM products WHERE name = 'Coffee'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 80, NOW()),
       ((SELECT id FROM products WHERE name = 'Tea'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 40, NOW()),
       ((SELECT id FROM products WHERE name = 'Sandwich'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 25, NOW()),
       ((SELECT id FROM products WHERE name = 'Salad'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 15, NOW()),
       ((SELECT id FROM products WHERE name = 'Cake Slice'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 10, NOW());

-- ORDERS

INSERT INTO orders (user_id, store_id, total_amount, status, created_at, updated_at)
VALUES ((SELECT id FROM users WHERE username = 'cashier1'), (SELECT id FROM stores WHERE name = 'Main Branch'), 10.00, 'COMPLETED', NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'manager1'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 7.50, 'COMPLETED', NOW(), NOW()),
       ((SELECT id FROM users WHERE username = 'cashier2'), (SELECT id FROM stores WHERE name = 'West Side Outlet'), 6.00, 'PENDING', NOW(), NOW());

-- ORDER ITEMS

INSERT INTO order_items (order_id, product_id, quantity, price, total)
VALUES ((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE username = 'cashier1') ORDER BY created_at DESC LIMIT 1), (SELECT id FROM products WHERE name = 'Coffee'), 2, 2.50, 5.00),
       ((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE username = 'cashier1') ORDER BY created_at DESC LIMIT 1), (SELECT id FROM products WHERE name = 'Tea'), 1, 2.50, 2.50),
       ((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE username = 'manager1') ORDER BY created_at DESC LIMIT 1), (SELECT id FROM products WHERE name = 'Sandwich'), 1, 5.00, 5.00),
       ((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE username = 'manager1') ORDER BY created_at DESC LIMIT 1), (SELECT id FROM products WHERE name = 'Tea'), 1, 2.50, 2.50),
       ((SELECT id FROM orders WHERE user_id = (SELECT id FROM users WHERE username = 'cashier2') ORDER BY created_at DESC LIMIT 1), (SELECT id FROM products WHERE name = 'Salad'), 2, 3.00, 6.00);

-- SALES REPORT

INSERT INTO sales_report (store_id, report_date, total_sales, total_orders, created_at)
VALUES ((SELECT id FROM stores WHERE name = 'Main Branch'), CURRENT_DATE, 10.00, 1, NOW()),
       ((SELECT id FROM stores WHERE name = 'West Side Outlet'), CURRENT_DATE, 7.50, 1, NOW());
