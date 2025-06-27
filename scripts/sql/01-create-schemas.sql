-- =======================================
-- 01-create-schemas.sql
-- =======================================

CREATE SCHEMA IF NOT EXISTS users AUTHORIZATION localpos_user;
CREATE SCHEMA IF NOT EXISTS products AUTHORIZATION localpos_user;
CREATE SCHEMA IF NOT EXISTS inventory AUTHORIZATION localpos_user;
CREATE SCHEMA IF NOT EXISTS orders AUTHORIZATION localpos_user;
CREATE SCHEMA IF NOT EXISTS reporting AUTHORIZATION localpos_user;
