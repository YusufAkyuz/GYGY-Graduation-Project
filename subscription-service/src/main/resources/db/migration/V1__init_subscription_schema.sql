CREATE TABLE subscriptions (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    order_id UUID NOT NULL UNIQUE,
    product_code VARCHAR(100) NOT NULL,
    msisdn VARCHAR(20) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    activated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_subscriptions_customer_id ON subscriptions (customer_id);

CREATE TABLE msisdn_pool (
    msisdn VARCHAR(20) PRIMARY KEY,
    allocated BOOLEAN NOT NULL DEFAULT FALSE
);

-- MVP: 5000-5099 aralığında 100 test numarası (5XXX XXX XX XX yerine kısa mock numaralar).
INSERT INTO msisdn_pool (msisdn)
SELECT '90555' || LPAD(n::text, 7, '0')
FROM generate_series(1, 100) AS n;

CREATE TABLE outbox_events (
    id UUID PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    processed_at TIMESTAMPTZ
);

CREATE INDEX idx_outbox_events_unprocessed ON outbox_events (created_at) WHERE processed_at IS NULL;

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    entity_name VARCHAR(100) NOT NULL,
    entity_id VARCHAR(100) NOT NULL,
    action VARCHAR(50) NOT NULL,
    actor_user_id VARCHAR(100),
    details TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
