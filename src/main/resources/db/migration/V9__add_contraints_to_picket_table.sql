ALTER TABLE picket
ADD CONSTRAINT PK_name_measurement_id UNIQUE (name, measurement_id),
ADD CONSTRAINT PK_Picket_id_name_measurement_id UNIQUE (picket_internal_id, name, measurement_id);