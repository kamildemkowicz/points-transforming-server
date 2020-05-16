ALTER TABLE measurement
ADD CONSTRAINT PK_Measurement_name UNIQUE (measurement_internal_id, version, name);
