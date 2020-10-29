CREATE TABLE geodetic_object (
 id int primary key auto_increment,
 name VARCHAR(255) NOT NULL,
 description VARCHAR(255),
 symbol VARCHAR(255) NOT NULL,
 color VARCHAR(255) NOT NULL,
 measurement_internal_id VARCHAR(255) NOT NULL
);

CREATE TABLE single_line (
 id int primary key auto_increment,
 start_picket_internal_id VARCHAR(255) NOT NULL,
 end_picket_internal_id VARCHAR(255) NOT NULL,
 geodetic_object_id int NOT NULL,
 CONSTRAINT PK_geodetic_object_start_end_pickets UNIQUE (start_picket_internal_id, end_picket_internal_id, geodetic_object_id)
);
