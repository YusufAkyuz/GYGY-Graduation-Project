CREATE TABLE notification_templates (
    code VARCHAR(100) PRIMARY KEY,
    channel VARCHAR(20) NOT NULL,
    content VARCHAR(1000) NOT NULL
);

CREATE TABLE notification_log (
    id UUID PRIMARY KEY,
    template_code VARCHAR(100) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    content VARCHAR(2000) NOT NULL,
    sent_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_notification_log_recipient ON notification_log (recipient);

INSERT INTO notification_templates (code, channel, content) VALUES
    ('WELCOME_SMS', 'SMS', 'TelcoX''e hoş geldiniz! Hattınız {{msisdn}} aktif edildi.'),
    ('INVOICE_EMAIL', 'EMAIL', '{{period}} dönemi faturanız {{amount}} TRY olarak oluşturuldu.'),
    ('QUOTA_WARNING_SMS', 'SMS', 'Kotanızın %{{percent}}''ini kullandınız.'),
    ('QUOTA_EXCEEDED_SMS', 'SMS', 'Kotanızı {{overageMb}} MB aştınız. Ek paket satın alabilirsiniz.'),
    ('TICKET_OPENED_SMS', 'SMS', 'Talebiniz alındı: {{subject}}'),
    ('CUSTOMER_REGISTERED_EMAIL', 'EMAIL', 'Kaydınız alındı, KYC onayı bekleniyor.');

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
