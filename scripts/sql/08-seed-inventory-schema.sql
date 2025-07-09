-- ========================================
-- Seed for inventory schema
-- ========================================

-- ========================================
-- inventory
-- ========================================
INSERT INTO inventory.inventory (id, product_id, store_id, quantity, created_at, updated_at) VALUES
  ('51bb1a58-0d26-4c1d-8262-19d28f9a236f', '7eaba064-6513-4b93-8568-7f1ee49d91a5', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 100, NOW(), NOW()),
  ('cdeebc7a-cf31-4a64-b18e-2d836d2e041b', 'ee2e4552-b4e9-4d28-89d1-26850f9a0e14', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 50, NOW(), NOW()),
  ('25f8e65e-9f84-4185-81ed-f446f4e33447', '49e7c947-4c63-4c52-8f87-3a3f4b5d8c34', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 30, NOW(), NOW()),
  ('2940aa1d-5f63-4261-8ff7-2737ec0da776', '2ca43245-547d-427e-81e3-7aeebdd968ec', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 20, NOW(), NOW()),
  ('1f4b242a-87be-4eaa-8f0f-2d312953e442', '4f8d5120-04b4-4f1b-9b0b-9cfa4aef96ab', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 15, NOW(), NOW()),
  ('b7f1d43a-9a6e-4b38-b6f0-35b6ca2b7061', '7eaba064-6513-4b93-8568-7f1ee49d91a5', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 80, NOW(), NOW()),
  ('c5a732bc-2441-48d1-bf38-79a91f7a69a5', 'ee2e4552-b4e9-4d28-89d1-26850f9a0e14', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 40, NOW(), NOW()),
  ('4a3724a9-757c-4052-88af-9b25e52f07c3', '49e7c947-4c63-4c52-8f87-3a3f4b5d8c34', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 25, NOW(), NOW()),
  ('1be37a3e-41a7-4f37-b66b-3e570f58a2e0', '2ca43245-547d-427e-81e3-7aeebdd968ec', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 15, NOW(), NOW()),
  ('d45327c9-0ca2-40b0-9314-9e429e32b650', '4f8d5120-04b4-4f1b-9b0b-9cfa4aef96ab', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 10, NOW(), NOW())
ON CONFLICT DO NOTHING;
