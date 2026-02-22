CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    card_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_transactions_card_id
ON transactions(card_id);
