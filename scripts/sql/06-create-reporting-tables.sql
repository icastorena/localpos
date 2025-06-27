-- ========================================
-- Tables for Reporting Microservice (Schema: reporting)
-- ========================================

CREATE TABLE IF NOT EXISTS reporting.sales_report (
    id           VARCHAR(36) PRIMARY KEY,
    store_id     VARCHAR(36) NOT NULL,
    report_date  DATE NOT NULL,
    total_sales  DECIMAL(14, 2) NOT NULL,
    total_orders INTEGER NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_sales_report_store FOREIGN KEY (store_id) REFERENCES users.stores (id)
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_sales_report_store_date ON reporting.sales_report (store_id, report_date);
