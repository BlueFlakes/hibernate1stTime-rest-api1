CREATE TABLE orders
(
  id        SERIAL NOT NULL
    CONSTRAINT orders_pkey
    PRIMARY KEY,
  amount    INTEGER,
  client_id INTEGER
    CONSTRAINT orders_customers__fk
    REFERENCES customers (id),
  dish_id   INTEGER
    CONSTRAINT orders_dishes__fk
    REFERENCES dishes (id)
);

CREATE UNIQUE INDEX orders_id_uindex
  ON orders (id);

