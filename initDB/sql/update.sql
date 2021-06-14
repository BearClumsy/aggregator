INSERT INTO aggregator.roles (roles)
VALUES ('admin');
INSERT INTO aggregator.roles (roles)
VALUES ('participant');
INSERT INTO aggregator.users (first_name, second_name, email, password, roles_id, active, login)
VALUES ('admin', 'admin', 'admin@test.com', '$2a$10$qNIZHEGD8CBX6NyGJ4GSjuPO5.gKbgqa2/ShyOBJOLhA.apLjOFgm', 1, true,
        'tester');

insert into aggregator.config (scanner_page_num)
values (2);
