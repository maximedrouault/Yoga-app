INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');


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