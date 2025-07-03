-- ========================================
-- Seed for users schema
-- ========================================

-- ========================================
-- roles
-- ========================================
INSERT INTO users.roles (id, name, description) VALUES
  ('d1b6b1b4-3c4a-4a4c-8d2a-9c6a4f40e523', 'ADMIN',   'Administrator with full permissions'),
  ('7af64d8f-f675-4d1c-a963-c77223815c1e', 'CASHIER', 'Cashier user with limited permissions'),
  ('51e7f073-7de2-40b3-b8de-4291093c87be', 'MANAGER', 'Store manager with intermediate permissions'),
  ('3c1a7744-9422-4c9a-84d3-c0d309f8d3f8', 'OWNER',   'Owner with highest level permissions'),
  ('c9276c02-e9a0-4d89-8506-789b6dcbf131', 'WAITER',  'Waiter with basic permissions')
ON CONFLICT DO NOTHING;

-- ========================================
-- users
-- ========================================
INSERT INTO users.users (id, username, password, email, first_name, last_name, phone, address, is_active, created_at, updated_at) VALUES
  ('8eec95c3-0b96-4b1b-9216-6a3a6f441b54', 'admin',     '$2a$10$GRc8bnRWVpYf0419z1x3HeadTolLbfKh4x2uZr3PY/LrWdtSfIB4.', 'admin@localpos.com',     'Iván',   'Castorena', '6141234567', 'Av. Tecnológico 1234, Chihuahua, Chih.',       true, NOW(), NOW()),
  ('b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', 'cashier1',  '$2a$10$3VWMK5xf5Wv6CmOQi/Xy5uxbGWcGL.0wGzNTGyJEdorak1SQg1dKG', 'cashier1@localpos.com',  'Laura',  'Perez',     '6141112233', 'Calle 10 #567, Col. Centro, Chihuahua, Chih.', true, NOW(), NOW()),
  ('dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', 'cashier2',  '$2a$10$R1wZ1MZR7Q.TdUuRf068T.sfZWMuyKYXbeqECULLNRNauRm60Yj66', 'cashier2@localpos.com',  'Pedro',  'Martinez',  '6142223344', 'Av. Independencia 456, Chihuahua, Chih.',      true, NOW(), NOW()),
  ('704a1fc9-b42e-4f7c-9bfc-53309a31e50b', 'manager1',  '$2a$10$rBIsEs70Ao/kus6psJNGhunBn5GTxJyk/8O8M161zfypBU1Ih5MlO', 'manager1@localpos.com',  'María',  'Alvarez',   '6143334455', 'Calle 5 de Mayo 789, Chihuahua, Chih.',        true, NOW(), NOW()),
  ('251ed3a1-c2c6-4a0a-bb45-5b8b08e5703a', 'owner1',    '$2a$12$t96OUJwl8LI43tU33B2Lu.IlUXIK9alTTQDf0.Y2ANXTrKqQeVR6K', 'owner1@localpos.com',    'Carlos', 'Duarte',    '6144445566', 'Blvd. Ortiz Mena 1011, Chihuahua, Chih.',      true, NOW(), NOW()),
  ('82779ee2-c306-480d-8c8e-8bbeed536d87', 'waiter1',   '$2a$10$0qJN8EEqgmGqittRqXPJbO2yLfUcnGLpEXbj5f9WeM.n0uuJBaYnC', 'waiter1@localpos.com',   'Ana',    'Torres',    '6145556677', 'Av. La Cantera 202, Chihuahua, Chih.',         true, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ========================================
-- user roles
-- ========================================
INSERT INTO users.user_roles (user_id, role_id) VALUES
  ('8eec95c3-0b96-4b1b-9216-6a3a6f441b54', 'd1b6b1b4-3c4a-4a4c-8d2a-9c6a4f40e523'),
  ('b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', '7af64d8f-f675-4d1c-a963-c77223815c1e'),
  ('dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', '7af64d8f-f675-4d1c-a963-c77223815c1e'),
  ('704a1fc9-b42e-4f7c-9bfc-53309a31e50b', '51e7f073-7de2-40b3-b8de-4291093c87be'),
  ('251ed3a1-c2c6-4a0a-bb45-5b8b08e5703a', '3c1a7744-9422-4c9a-84d3-c0d309f8d3f8'),
  ('82779ee2-c306-480d-8c8e-8bbeed536d87', 'c9276c02-e9a0-4d89-8506-789b6dcbf131')
ON CONFLICT DO NOTHING;

-- ========================================
-- stores
-- ========================================
INSERT INTO users.stores (id, code, name, address, created_at, updated_at) VALUES
  ('1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 'MAIN', 'Main Branch', '123 Main St, Cityville', NOW(), NOW()),
  ('963e2cb6-8f97-4e6c-91c1-03b70f195911', 'WEST', 'West Side Outlet', '450 Market St, West District', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ========================================
-- user stores
-- ========================================
INSERT INTO users.user_stores (user_id, store_id) VALUES
  ('8eec95c3-0b96-4b1b-9216-6a3a6f441b54', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60'),
  ('b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60'),
  ('704a1fc9-b42e-4f7c-9bfc-53309a31e50b', '963e2cb6-8f97-4e6c-91c1-03b70f195911'),
  ('dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', '963e2cb6-8f97-4e6c-91c1-03b70f195911'),
  ('251ed3a1-c2c6-4a0a-bb45-5b8b08e5703a', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60'),
  ('82779ee2-c306-480d-8c8e-8bbeed536d87', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60')
ON CONFLICT DO NOTHING;
