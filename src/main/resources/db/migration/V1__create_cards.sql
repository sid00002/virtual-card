CREATE TABLE cards (
    id UUID PRIMARY KEY,
    cardholder_name VARCHAR(255) NOT NULL,
    balance NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);
