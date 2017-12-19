-- auto-generated definition
CREATE TABLE dishes
(
  id    SERIAL NOT NULL
    CONSTRAINT dishes_pkey
    PRIMARY KEY,
  name  TEXT,
  price DOUBLE PRECISION
);

CREATE UNIQUE INDEX dishes_id_uindex
  ON dishes (id);

