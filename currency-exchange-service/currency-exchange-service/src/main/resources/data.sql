CREATE TABLE currency_exchange (
    id BIGINT PRIMARY KEY,
    currency_from VARCHAR(50),
    currency_to VARCHAR(50),
    conversion_multiple DECIMAL(19,2),
    environment VARCHAR(50)
);


INSERT INTO currency_exchange (id, currency_from, currency_to, conversion_multiple, environment)
VALUES (1001, 'USD', 'VND', 25000, '8000');

INSERT INTO currency_exchange (id, currency_from, currency_to, conversion_multiple, environment)
VALUES (1002, 'EUR', 'VND', 27000, '8000');

INSERT INTO currency_exchange (id, currency_from, currency_to, conversion_multiple, environment)
VALUES (1003, 'GBP', 'VND', 30000, '8000');
