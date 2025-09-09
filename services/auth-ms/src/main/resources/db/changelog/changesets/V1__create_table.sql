CREATE EXTENSION IF NOT EXISTS pgcrypto; -- эт нужно для gen_random_uuid()

CREATE TABLE roles
(
    id        serial PRIMARY KEY,
    role_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE auth_users
(
    id         UUID      DEFAULT gen_random_uuid() PRIMARY KEY,
    username   varchar(255) NOT NULL UNIQUE,
    password   varchar(512) NOT NULL,
    is_active  boolean   DEFAULT true,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    deleted_at timestamp,
    last_login timestamp    NULL
);

CREATE INDEX idx_auth_users_username ON auth_users (username);

CREATE TABLE IF NOT EXISTS auth_user_roles
(
    user_id UUID    NOT NULL REFERENCES auth_users (id) ON DELETE CASCADE,
    role_id integer NOT NULL REFERENCES roles (id) ON DELETE RESTRICT,
    PRIMARY KEY (user_id, role_id)
);