CREATE EXTENSION IF NOT EXISTS pgcrypto; -- эт нужно для gen_random_uuid()

CREATE TABLE profile
(
    id         uuid PRIMARY KEY,
    name       varchar(100)                           NOT NULL,
    surname    varchar(100)                           NOT NULL,
    age        integer CHECK (age BETWEEN 0 AND 100 ) NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP
);

