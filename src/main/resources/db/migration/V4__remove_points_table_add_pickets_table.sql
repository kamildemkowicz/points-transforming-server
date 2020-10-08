drop table if exists point;
drop table if exists measurements_points;

drop table if exists picket;

create table picket(
    id int primary key auto_increment,
    picket_internal_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    coordinateX DOUBLE NOT NULL,
    coordinateY DOUBLE NOT NULL,
    coordinate_x_2000 DOUBLE,
    coordinate_y_2000 DOUBLE,
    measurement_id int NOT NULL
);
