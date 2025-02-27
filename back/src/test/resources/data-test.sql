INSERT INTO TEACHERS (first_name, last_name, created_at, updated_at)
VALUES ('Margot', 'DELAHAYE', '2023-01-01T00:00:00', '2023-02-15T12:30:00'),
       ('Hélène', 'THIERCELIN', '2023-03-01T00:00:00', '2023-04-15T12:30:00');


INSERT INTO USERS (first_name, last_name, admin, email, password, created_at, updated_at)
VALUES ('Admin',
        'Admin',
        true,
        'yoga@studio.com',
        '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq',
        '2023-01-01T00:00:00',
        '2023-02-15T12:30:00'
       ),
       ('John',
        'Doe',
        false,
        'john.doe@example.com',
        '$2y$10$wwbYrEY27MPuKdkweVCFMuFsPrNHTfLm3G6JuLkdkUAXilvuIg/Ve',
        '2023-01-01T00:00:00',
        '2023-02-15T12:30:00'
       );

INSERT INTO SESSIONS (name, description, date, teacher_id, created_at, updated_at)
VALUES ('Yoga', 'Yoga session', '2023-01-01T10:00:00', 1, '2023-01-01T08:00:00', '2023-02-15T12:30:00'),
       ('Pilates', 'Pilates session', '2023-01-01T11:00:00', 2, '2023-01-01T08:05:00', '2023-02-15T12:30:00');

INSERT INTO PARTICIPATE (user_id, session_id)
VALUES (1, 1),
       (2, 2);