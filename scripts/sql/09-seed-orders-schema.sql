-- ========================================
-- Seed for orders schema
-- ========================================


-- ========================================
-- orders
-- ========================================
INSERT INTO orders.orders (id, user_id, store_id, total_amount, status, payment_method, order_type, created_at, updated_at) VALUES
  ('8c138a9c-bb6a-40ca-81ef-2dfde9b03c79', 'b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', '1bb3b69f-62d2-4b52-b8c2-8355057e8d60', 10.00, 'COMPLETED', 'CASH', 'SALE', NOW(), NOW()),
  ('2d863b0a-24ec-4fdb-9d63-4c5a9ae1226b', '704a1fc9-b42e-4f7c-9bfc-53309a31e50b', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 7.50, 'COMPLETED', 'CASH', 'SALE', NOW(), NOW()),
  ('d1a15dbd-0fcd-44e3-8b88-e4c4f0b3e9b9', 'dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', '963e2cb6-8f97-4e6c-91c1-03b70f195911', 6.00, 'PENDING', 'CASH', 'SALE', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- ========================================
-- order items
-- ========================================
INSERT INTO orders.order_items (id, order_id, product_id, quantity, price, total) VALUES
  ('121d7877-7700-44a0-aefb-41718b6d90bc', '8c138a9c-bb6a-40ca-81ef-2dfde9b03c79', '7eaba064-6513-4b93-8568-7f1ee49d91a5', 2, 2.50, 5.00),
  ('63db2066-e912-48f6-8724-5eeb1ec7f6dc', '8c138a9c-bb6a-40ca-81ef-2dfde9b03c79', 'ee2e4552-b4e9-4d28-89d1-26850f9a0e14', 1, 2.50, 2.50),
  ('8a422c5f-255d-4185-bad7-8eb913b19b9e', '2d863b0a-24ec-4fdb-9d63-4c5a9ae1226b', '49e7c947-4c63-4c52-8f87-3a3f4b5d8c34', 1, 5.00, 5.00),
  ('b4485d72-bf6b-4032-82c6-2627e62cd403', '2d863b0a-24ec-4fdb-9d63-4c5a9ae1226b', 'ee2e4552-b4e9-4d28-89d1-26850f9a0e14', 1, 2.50, 2.50),
  ('218aa3bb-0975-4bca-83b0-41e385d98ebf', 'd1a15dbd-0fcd-44e3-8b88-e4c4f0b3e9b9', '2ca43245-547d-427e-81e3-7aeebdd968ec', 2, 3.00, 6.00)
ON CONFLICT DO NOTHING;
