-- ========================================
-- Seed for products schema
-- ========================================

-- ========================================
-- categories
-- ========================================
INSERT INTO products.categories (id, name, description) VALUES
  ('c9aef7db-5d9e-4a3b-b326-1c3f4d831f8b', 'Beverages', 'Drinks and refreshments'),
  ('f81f118d-1c18-41ed-baf8-44e0e6e7ca72', 'Food', 'Food items and snacks'),
  ('6a428fce-2cd5-4f2b-a1d4-0a64a93a6281', 'Desserts', 'Sweet treats and desserts')
ON CONFLICT DO NOTHING;

-- ========================================
-- products
-- ========================================
INSERT INTO products.products (id, name, description, price, category_id, created_at, updated_at) VALUES
  ('7eaba064-6513-4b93-8568-7f1ee49d91a5', 'Coffee', 'Fresh brewed coffee', 2.50, 'c9aef7db-5d9e-4a3b-b326-1c3f4d831f8b', NOW(), NOW()),
  ('ee2e4552-b4e9-4d28-89d1-26850f9a0e14', 'Tea', 'Organic green tea', 2.00, 'c9aef7db-5d9e-4a3b-b326-1c3f4d831f8b', NOW(), NOW()),
  ('49e7c947-4c63-4c52-8f87-3a3f4b5d8c34', 'Sandwich', 'Ham and cheese sandwich', 5.00, 'f81f118d-1c18-41ed-baf8-44e0e6e7ca72', NOW(), NOW()),
  ('2ca43245-547d-427e-81e3-7aeebdd968ec', 'Salad', 'Fresh garden salad', 4.50, 'f81f118d-1c18-41ed-baf8-44e0e6e7ca72', NOW(), NOW()),
  ('4f8d5120-04b4-4f1b-9b0b-9cfa4aef96ab', 'Cake Slice', 'Chocolate cake slice', 3.50, '6a428fce-2cd5-4f2b-a1d4-0a64a93a6281', NOW(), NOW())
ON CONFLICT DO NOTHING;
