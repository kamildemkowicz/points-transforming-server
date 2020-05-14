ALTER table measurement
ADD COLUMN owner VARCHAR(255) NOT NULL;

UPDATE measurement SET owner="Kamil Demkowicz"