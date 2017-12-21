CREATE TABLE IF NOT EXISTS customers
(
  id      SERIAL NOT NULL
    CONSTRAINT customers_pkey
    PRIMARY KEY,
  name    TEXT,
  surname TEXT,
  email   TEXT
);

CREATE UNIQUE INDEX customers_id_uindex
  ON customers (id);

