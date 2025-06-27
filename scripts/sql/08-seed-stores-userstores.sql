-- ========================================
-- Seed Stores and User Stores
-- ========================================

-- STORES
INSERT INTO users.stores (id, code, name, address, created_at, updated_at) VALUES
  ('1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 'MAIN', 'Main Branch', '123 Main St, Cityville', NOW(), NOW()),
  ('963e2cb6-8f97-4e6c-91c1-03b70f195911', 'WEST', 'West Side Outlet', '450 Market St, West District', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- USER STORES (asignación usuarios a tiendas)
INSERT INTO users.user_stores (user_id, store_id) VALUES
  ('8eec95c3-0b96-4b1b-9216-6a3a6f441b54', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60'),
  ('b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60'),
  ('704a1fc9-b42e-4f7c-9bfc-53309a31e50b', '963e2cb6-8f97-4e6c-91c1-03b70f195911'),
  ('dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', '963e2cb6-8f97-4e6c-91c1-03b70f195911'),
  ('251ed3a1-c2c6-4a0a-bb45-5b8b08e5703a', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60')
ON CONFLICT DO NOTHING;
