CREATE TABLE IF NOT EXISTS accounts (
    id UUID PRIMARY KEY,
    account_number VARCHAR(32) UNIQUE NOT NULL,
    owner_name VARCHAR(128) NOT NULL,
    balance NUMERIC(15, 2) NOT NULL CHECK (balance >= 0.00),
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS transactions (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES accounts(id),
    amount NUMERIC(15, 2) NOT NULL,
    type VARCHAR(16) NOT NULL,
    category VARCHAR(64) NOT NULL,
    reference_code VARCHAR(64) UNIQUE NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_tx_acc_time ON transactions(account_id, timestamp DESC);
