drop table if exists point;
drop table if exists measurements_points;

create table point(
    id int primary key auto_increment,
    coordinateX DOUBLE NOT NULL,
    coordinateY DOUBLE NOT NULL
);

create table measurements_points(
    measurement_id int NOT NULL,
    point_id int NOT NULL
);

INSERT INTO point (coordinateX, coordinateY) VALUES
(20.20, 10),
(30.20, 10.10),
(40.20, 30.30),
(50.20, 50.40),
(60.20, 10.560),
(70.20, 30.40);

INSERT INTO measurements_points (measurement_id, point_id) VALUES
(1, 1),
(1, 2),
(2, 1),
(2, 3);