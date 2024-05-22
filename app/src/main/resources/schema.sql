CREATE TABLE IF NOT EXISTS orders (
      id UUID NOT NULL,
      status varchar(50) NOT NULL,
      PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS order_items (
      id UUID NOT NULL,
      order_id UUID NOT NULL,
      status varchar(50) NOT NULL,
      product varchar(50) NOT NULL,
      quantity integer NOT NULL,
      PRIMARY KEY (id),
      FOREIGN KEY (order_id) REFERENCES orders(id)
);
