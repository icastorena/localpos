-- ========================================
-- Tables for Users Microservice (Schema: users)
-- ========================================

CREATE TABLE IF NOT EXISTS users.roles (
    id          VARCHAR(36) PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users.users (
    id           VARCHAR(36) PRIMARY KEY,
    username     VARCHAR(100) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    email        VARCHAR(150) UNIQUE,
    first_name   VARCHAR(100) NOT NULL,
    last_name    VARCHAR(100) NOT NULL,
    phone        VARCHAR(20),
    address      TEXT,
    is_active    BOOLEAN NOT NULL DEFAULT true,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users.user_roles (
    user_id VARCHAR(36) NOT NULL,
    role_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users.users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES users.roles (id)
);

CREATE TABLE IF NOT EXISTS users.stores (
    id         VARCHAR(36) PRIMARY KEY,
    code       VARCHAR(4) NOT NULL UNIQUE,
    name       VARCHAR(150) NOT NULL,
    address    TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users.user_stores (
    user_id  VARCHAR(36) NOT NULL,
    store_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (user_id, store_id),
    CONSTRAINT fk_user_stores_user FOREIGN KEY (user_id) REFERENCES users.users (id),
    CONSTRAINT fk_user_stores_store FOREIGN KEY (store_id) REFERENCES users.stores (id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_users_username ON users.users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users.users (email);
CREATE INDEX IF NOT EXISTS idx_roles_name ON users.roles (name);
