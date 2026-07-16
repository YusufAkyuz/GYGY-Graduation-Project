-- common modülündeki CommonJpaAutoConfiguration, JPA'lı her serviste outbox_events'i
-- yönetilen bir entity olarak taradığından (Kafka bağımlılığı olmasa dahi), tüm servislerin
-- şemasında bu tablo bulunmalıdır (TR-03).
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
