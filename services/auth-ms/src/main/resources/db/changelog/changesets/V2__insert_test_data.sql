INSERT INTO roles (role_name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

INSERT INTO auth_users (id, username, password, is_active)
VALUES ('5bc388a6-f95d-459d-b5b5-51b63e01c533', 'admin1', '$2a$10$CCqJSRQPh9ZAA5WXpICRpekyLAFIlW0A35gGrZ.1eVXMSOgXtZfOi', true),
       ('8b62d449-4312-48ed-82ef-f9754ca4858b', 'user1', '$2a$10$CCqJSRQPh9ZAA5WXpICRpekyLAFIlW0A35gGrZ.1eVXMSOgXtZfOi', true),
       ('21fabe35-28cc-41f5-be18-d08bc97cb584', 'user2', '$2a$10$CCqJSRQPh9ZAA5WXpICRpekyLAFIlW0A35gGrZ.1eVXMSOgXtZfOi', true),
       ('8ef8da83-5137-48ed-aa72-b82ce10bfbd3', 'manager1', '$2a$10$CCqJSRQPh9ZAA5WXpICRpekyLAFIlW0A35gGrZ.1eVXMSOgXtZfOi', true),
       ('f73185e5-99a6-45d5-bc62-15172237615f', 'inactive_user', '$2a$10$CCqJSRQPh9ZAA5WXpICRpekyLAFIlW0A35gGrZ.1eVXMSOgXtZfOi', false);

INSERT INTO auth_user_roles (user_id, role_id)
VALUES ((SELECT id FROM auth_users WHERE username = 'admin1'), 1),
       ((SELECT id FROM auth_users WHERE username = 'admin1'), 2),
       ((SELECT id FROM auth_users WHERE username = 'user1'), 2),
       ((SELECT id FROM auth_users WHERE username = 'user2'), 2),
       ((SELECT id FROM auth_users WHERE username = 'manager1'), 1),
       ((SELECT id FROM auth_users WHERE username = 'manager1'), 2),
       ((SELECT id FROM auth_users WHERE username = 'inactive_user'), 1);