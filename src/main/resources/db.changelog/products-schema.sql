
CREATE TABLE product (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         price DECIMAL(10,2) NOT NULL
);

CREATE TABLE order_product (
                               order_id BIGINT NOT NULL,
                               product_id BIGINT NOT NULL,
                               quantity INT NOT NULL DEFAULT 1,
                               PRIMARY KEY (order_id, product_id),
                               CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES "order" (id) ON DELETE CASCADE,
                               CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);