CREATE TABLE species (
    species_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(100) NOT NULL UNIQUE,
    category       VARCHAR(100),
    food           VARCHAR(100),
    daily_food_kg  NUMERIC(6,2) NOT NULL CHECK (daily_food_kg >= 0)
);

CREATE TABLE enclosure (
    enclosure_id  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    size_m2       INTEGER NOT NULL CHECK (size_m2 > 0),
    description   VARCHAR(500)
);

CREATE TABLE animal (
    animal_id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    date_of_birth  DATE,
    species_id     BIGINT NOT NULL REFERENCES species (species_id),
    enclosure_id   BIGINT REFERENCES enclosure (enclosure_id)
);