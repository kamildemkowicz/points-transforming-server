drop table if exists point;
drop table if exists measurements_points;

drop table if exists picket;

create table picket(
    id int primary key auto_increment,
    picket_id VARCHAR(255) NOT NULL,
    coordinateX DOUBLE NOT NULL,
    coordinateY DOUBLE NOT NULL,
    measurement_id int NOT NULL
);

INSERT INTO picket (picket_id, coordinateX, coordinateY, measurement_id) VALUES
("1", 20.20, 10, 1),
("2", 30.20, 10.10, 1),
("1", 40.20, 30.30, 2),
("2", 50.20, 50.40, 2),
("1", 60.20, 10.560, 3),
("2", 70.20, 30.40, 3);
