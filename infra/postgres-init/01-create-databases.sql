-- Database-per-service (TR-02): her mikroservis için ayrı veritabanı.
-- Lokal geliştirmede tek PostgreSQL instance'ı üzerinde ayrı DB'ler olarak modellenir.
CREATE DATABASE identity_db;
CREATE DATABASE customer_db;
CREATE DATABASE product_catalog_db;
CREATE DATABASE order_db;
CREATE DATABASE subscription_db;
CREATE DATABASE usage_db;
CREATE DATABASE billing_db;
CREATE DATABASE payment_db;
CREATE DATABASE notification_db;
CREATE DATABASE ticket_db;
