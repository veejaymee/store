CREATE INDEX idx_customer_name ON customer(name);

CREATE INDEX idx_order_id ON "order"(id);

CREATE INDEX idx_order_product ON order_product(product_id, order_id);