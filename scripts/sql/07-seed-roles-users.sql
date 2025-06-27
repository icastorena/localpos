-- ========================================
-- Seed Roles and Users
-- ========================================

-- ROLES
INSERT INTO users.roles (id, name, description) VALUES
  ('d1b6b1b4-3c4a-4a4c-8d2a-9c6a4f40e523', 'ADMIN', 'Administrator with full permissions'),
  ('7af64d8f-f675-4d1c-a963-c77223815c1e', 'CASHIER', 'Cashier user with limited permissions'),
  ('51e7f073-7de2-40b3-b8de-4291093c87be', 'MANAGER', 'Store manager with intermediate permissions'),
  ('3c1a7744-9422-4c9a-84d3-c0d309f8d3f8', 'OWNER', 'Owner with highest level permissions')
ON CONFLICT DO NOTHING;

-- USERS
INSERT INTO users.users (id, username, password, email, created_at, updated_at) VALUES
  ('8eec95c3-0b96-4b1b-9216-6a3a6f441b54', 'admin', '$2a$10$GRc8bnRWVpYf0419z1x3HeadTolLbfKh4x2uZr3PY/LrWdtSfIB4.', 'admin@localpos.com', NOW(), NOW()),
  ('b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', 'cashier1', '$2a$10$3VWMK5xf5Wv6CmOQi/Xy5uxbGWcGL.0wGzNTGyJEdorak1SQg1dKG', 'cashier1@localpos.com', NOW(), NOW()),
  ('704a1fc9-b42e-4f7c-9bfc-53309a31e50b', 'manager1', '$2a$10$rBIsEs70Ao/kus6psJNGhunBn5GTxJyk/8O8M161zfypBU1Ih5MlO', 'manager1@localpos.com', NOW(), NOW()),
  ('dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', 'cashier2', '$2a$10$R1wZ1MZR7Q.TdUuRf068T.sfZWMuyKYXbeqECULLNRNauRm60Yj66', 'cashier2@localpos.com', NOW(), NOW()),
  ('251ed3a1-c2c6-4a0a-bb45-5b8b08e5703a', 'owner1', '$2a$12$t96OUJwl8LI43tU33B2Lu.IlUXIK9alTTQDf0.Y2ANXTrKqQeVR6K', 'owner1@localpos.com', NOW(), NOW())
ON CONFLICT DO NOTHING;

-- USER ROLES
INSERT INTO users.user_roles (user_id, role_id) VALUES
  ('8eec95c3-0b96-4b1b-9216-6a3a6f441b54', 'd1b6b1b4-3c4a-4a4c-8d2a-9c6a4f40e523'),
  ('b2cb3e13-6b32-4b81-8a37-fc0b37aa5db4', '7af64d8f-f675-4d1c-a963-c77223815c1e'),
  ('dd6a8d38-9d0a-4ea2-b04c-cd5ff5ff1d82', '7af64d8f-f675-4d1c-a963-c77223815c1e'),
  ('704a1fc9-b42e-4f7c-9bfc-53309a31e50b', '51e7f073-7de2-40b3-b8de-4291093c87be'),
  ('251ed3a1-c2c6-4a0a-bb45-5b8b08e5703a', '3c1a7744-9422-4c9a-84d3-c0d309f8d3f8')
ON CONFLICT DO NOTHING;
