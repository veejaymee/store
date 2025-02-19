
SELECT setval('customer_id_seq', (SELECT MAX(id) FROM customer), true);

SELECT setval('order_id_seq', (SELECT MAX(id) FROM "order"), true);

SELECT setval('product_id_seq', (SELECT MAX(id) FROM product), true);

SELECT currval('customer_id_seq');

SELECT currval('order_id_seq');

SELECT currval('product_id_seq');
