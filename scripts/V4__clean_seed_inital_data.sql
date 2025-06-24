-- ================================
-- Clean and Reset IDs - POS System
-- ================================
-- ORDER MODULE
DELETE FROM order_items;

DELETE FROM orders;

ALTER SEQUENCE order_items_id_seq restart WITH 1;

ALTER SEQUENCE orders_id_seq restart WITH 1;

-- INVENTORY MODULE
DELETE FROM inventory;

ALTER SEQUENCE inventory_id_seq restart WITH 1;

-- REPORTING MODULE
DELETE FROM sales_report;

ALTER SEQUENCE sales_report_id_seq restart WITH 1;

-- PRODUCT MODULE
DELETE FROM products;

DELETE FROM categories;

ALTER SEQUENCE products_id_seq restart WITH 1;

ALTER SEQUENCE categories_id_seq restart WITH 1;

-- USER MODULE
DELETE FROM user_stores;

DELETE FROM user_roles;

DELETE FROM users;

DELETE FROM ROLES;

DELETE FROM stores;

ALTER SEQUENCE users_id_seq restart WITH 1;

ALTER SEQUENCE roles_id_seq restart WITH 1;

ALTER SEQUENCE stores_id_seq restart WITH 1;
