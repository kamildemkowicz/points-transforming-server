ALTER table measurement
ADD COLUMN creation_date DATETIME NOT NULL,
ADD COLUMN end_date DATETIME,
ADD COLUMN place VARCHAR(255) NOT NULL;