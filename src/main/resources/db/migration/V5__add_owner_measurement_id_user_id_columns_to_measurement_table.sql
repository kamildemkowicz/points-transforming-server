ALTER table measurement
ADD COLUMN measurement_internal_id VARCHAR(255) NOT NULL,
ADD COLUMN owner VARCHAR(255) NOT NULL,
ADD COLUMN version int NOT NULL,
ADD COLUMN user_id int NOT NULL;
