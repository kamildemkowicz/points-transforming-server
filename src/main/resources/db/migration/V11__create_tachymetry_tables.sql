CREATE TABLE tachymetry (
 id int primary key auto_increment,
 name VARCHAR(255) NOT NULL,
 tachymetr_type VARCHAR(255) NOT NULL,
 temperature DOUBLE,
 pressure BIGINT,
 measurement_internal_id VARCHAR(255) NOT NULL,
 CONSTRAINT PK_tach_name_measurement_id UNIQUE (name, measurement_internal_id)
);

CREATE TABLE measuring_station (
 id int primary key auto_increment,
 station_number BIGINT NOT NULL,
 station_name VARCHAR(255) NOT NULL,
 starting_point_internal_id VARCHAR(255) NOT NULL,
 end_point_internal_id VARCHAR(255) NOT NULL,
 tachymetry_id int NOT NULL,
 CONSTRAINT PK_station_number_tachymetry_id UNIQUE (station_number, tachymetry_id),
 CONSTRAINT PK_station_name_tachymetry_id UNIQUE (station_name, tachymetry_id)
);

CREATE TABLE tachymetry_picket_measured (
 id int primary key auto_increment,
 distance DOUBLE NOT NULL,
 angle DOUBLE NOT NULL,
 measuring_station_id int NOT NULL,
 picket_internal_id VARCHAR(255) NOT NULL
);
