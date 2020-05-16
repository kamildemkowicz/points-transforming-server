CREATE TABLE users (
    id int primary key auto_increment,
    user_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    CONSTRAINT PK_User_name UNIQUE (user_name)
);
