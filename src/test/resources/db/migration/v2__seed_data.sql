-- Seed a handful of users that your tests can read
INSERT INTO users (id, username, email, full_name) VALUES
    ('11111111-1111-1111-1111-111111111111', 'it_user',        'it@example.com',        'IT Admin'),
    ('22222222-2222-2222-2222-222222222222', 'marketing_user','marketing@example.com','Marketing Lead'),
    ('33333333-3333-3333-3333-333333333333', 'hr_user',       'hr@example.com',       'HR Manager');

-- Seed a handful of calendars that your tests can read
INSERT INTO calendar (name, owner_id) VALUES
    ('IT Calendar',     '11111111-1111-1111-1111-111111111111'),
    ('Marketing Calendar', '22222222-2222-2222-2222-222222222222'),
    ('HR Calendar',        '33333333-3333-3333-3333-333333333333');