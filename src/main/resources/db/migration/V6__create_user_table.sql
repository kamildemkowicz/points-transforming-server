CREATE TABLE users (
    id int primary key auto_increment,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(50) NOT NULL,
    CONSTRAINT PK_User_name UNIQUE (username),
    CONSTRAINT PK_email UNIQUE (email)
);

CREATE TABLE roles (
    id int primary key auto_increment,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE user_roles (
    user_id int NOT NULL,
    role_id int NOT NULL
);

INSERT INTO roles(name) VALUES('ROLE_ADMIN');
INSERT INTO roles(name) VALUES('ROLE_USER');
