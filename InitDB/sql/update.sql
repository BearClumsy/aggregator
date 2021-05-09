INSERT INTO aggregator.role (role)
VALUES ('admin');
INSERT INTO aggregator.role (role)
VALUES ('participant');
INSERT INTO aggregator.users (first_name, second_name, email, password, role_id, active, login)
VALUES ('admin', 'admin', 'admin@test.com', '$2a$10$qNIZHEGD8CBX6NyGJ4GSjuPO5.gKbgqa2/ShyOBJOLhA.apLjOFgm', 1, true,
        'tester');
INSERT INTO aggregator.user_role (user_id, role_id) VALUES (1, 1);