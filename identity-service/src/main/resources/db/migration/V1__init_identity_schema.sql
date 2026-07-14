CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    actor_user_id VARCHAR(100),
    details TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

INSERT INTO roles (name) VALUES ('ROLE_ADMIN'), ('ROLE_CUSTOMER'), ('ROLE_AGENT'), ('ROLE_BILLING_OPERATOR');

-- Varsayılan admin: username=admin, password=Admin123! (BCrypt) — sadece lokal/dev ortam için.
INSERT INTO users (id, username, password_hash, email, status)
VALUES ('00000000-0000-0000-0000-000000000001', 'admin',
        '$2a$10$n/FPH/rRdrZ58VoHUC.RfuBiclZ1NR8bs3leBNyZTBvz/KEOduDru',
        'admin@telcox.local', 'ACTIVE');

INSERT INTO user_roles (user_id, role_id)
SELECT '00000000-0000-0000-0000-000000000001', id FROM roles WHERE name = 'ROLE_ADMIN';
