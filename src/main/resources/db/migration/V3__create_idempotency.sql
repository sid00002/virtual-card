CREATE TABLE idempotency_records (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(255) NOT NULL,
    endpoint VARCHAR(255) NOT NULL,
    response_body TEXT,
    http_status INT,
    created_at TIMESTAMP NOT NULL,
    UNIQUE (idempotency_key, endpoint)
);
