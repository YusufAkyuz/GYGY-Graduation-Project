CREATE TABLE usage_balances (
    subscription_id UUID PRIMARY KEY,
    msisdn VARCHAR(20) NOT NULL,
    quota_mb NUMERIC(12,2) NOT NULL,
    used_mb NUMERIC(12,2) NOT NULL DEFAULT 0,
    overage_mb NUMERIC(12,2) NOT NULL DEFAULT 0,
    threshold_80_notified BOOLEAN NOT NULL DEFAULT FALSE,
    threshold_100_notified BOOLEAN NOT NULL DEFAULT FALSE
);

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
