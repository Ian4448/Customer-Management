create table customer
(
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    user_name  VARCHAR(255) NOT NULL PRIMARY KEY,
    password   VARCHAR(255) NOT NULL
);
