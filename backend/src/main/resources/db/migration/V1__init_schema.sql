-- V1__init_schema.sql
-- Multi-Tenant AI Bill Processing Schema

CREATE TABLE IF NOT EXISTS organizations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    parent_id BIGINT REFERENCES organizations(id) ON DELETE RESTRICT,
    org_path VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_organizations_parent_id ON organizations(parent_id);
CREATE INDEX idx_organizations_org_path ON organizations(org_path);

CREATE TABLE IF NOT EXISTS tenant_quotas (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL UNIQUE REFERENCES organizations(id) ON DELETE CASCADE,
    monthly_token_limit BIGINT NOT NULL DEFAULT 1000000,
    tokens_used_this_month BIGINT NOT NULL DEFAULT 0,
    monthly_cost_cap_usd NUMERIC(10,2) NOT NULL DEFAULT 50.00,
    cost_accumulated_usd NUMERIC(10,4) NOT NULL DEFAULT 0.0000,
    last_reset_date DATE NOT NULL DEFAULT CURRENT_DATE,
    is_throttled BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    role VARCHAR(50) NOT NULL, -- CLERK, APPROVER, ADMIN
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_tenant_id ON users(tenant_id);
CREATE INDEX idx_users_username ON users(username);

CREATE TABLE IF NOT EXISTS bills (
    id BIGSERIAL PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    submitted_by_user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    invoice_number VARCHAR(100) NOT NULL,
    vendor_name VARCHAR(150) NOT NULL,
    vendor_tax_id VARCHAR(50),
    category VARCHAR(50) NOT NULL,
    bill_date DATE NOT NULL,
    due_date DATE,
    currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    subtotal_amount NUMERIC(14,2) NOT NULL DEFAULT 0.00,
    tax_amount NUMERIC(14,2) NOT NULL DEFAULT 0.00,
    total_amount NUMERIC(14,2) NOT NULL,
    status VARCHAR(50) NOT NULL, -- DRAFT, PENDING, IN_REVIEW, APPROVED, REJECTED, PAID
    file_url VARCHAR(500),
    file_hash VARCHAR(128),
    notes TEXT,
    rejection_reason TEXT,
    is_anomaly BOOLEAN NOT NULL DEFAULT FALSE,
    anomaly_severity VARCHAR(20), -- LOW, MEDIUM, HIGH, CRITICAL
    anomaly_reasons TEXT,
    ai_confidence_score NUMERIC(5,2),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Compound and targeted indexes for tenant isolation & high-throughput dashboards
CREATE INDEX idx_bills_tenant_id ON bills(tenant_id);
CREATE INDEX idx_bills_tenant_status ON bills(tenant_id, status);
CREATE INDEX idx_bills_tenant_bill_date ON bills(tenant_id, bill_date DESC);
CREATE INDEX idx_bills_tenant_vendor_invoice ON bills(tenant_id, vendor_name, invoice_number);
CREATE INDEX idx_bills_tenant_file_hash ON bills(tenant_id, file_hash);
CREATE INDEX idx_bills_is_anomaly ON bills(tenant_id, is_anomaly);

CREATE TABLE IF NOT EXISTS bill_items (
    id BIGSERIAL PRIMARY KEY,
    bill_id BIGINT NOT NULL REFERENCES bills(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    description VARCHAR(255) NOT NULL,
    quantity NUMERIC(10,2) NOT NULL DEFAULT 1,
    unit_price NUMERIC(14,2) NOT NULL,
    total_price NUMERIC(14,2) NOT NULL
);

CREATE INDEX idx_bill_items_bill_id ON bill_items(bill_id);
CREATE INDEX idx_bill_items_tenant_id ON bill_items(tenant_id);

CREATE TABLE IF NOT EXISTS bill_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    bill_id BIGINT NOT NULL REFERENCES bills(id) ON DELETE CASCADE,
    tenant_id BIGINT NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    action VARCHAR(50) NOT NULL,
    performed_by_user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    comment TEXT,
    timestamp TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_audit_bill_id ON bill_audit_logs(bill_id);
CREATE INDEX idx_audit_tenant_id ON bill_audit_logs(tenant_id);

CREATE TABLE IF NOT EXISTS ai_extraction_jobs (
    id UUID PRIMARY KEY,
    tenant_id BIGINT NOT NULL REFERENCES organizations(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    status VARCHAR(50) NOT NULL,
    filename VARCHAR(255),
    extracted_data_json TEXT,
    error_message TEXT,
    tokens_consumed INTEGER DEFAULT 0,
    estimated_cost_usd NUMERIC(8,4) DEFAULT 0.0000,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    completed_at TIMESTAMP
);

CREATE INDEX idx_ai_jobs_tenant_id ON ai_extraction_jobs(tenant_id);
CREATE INDEX idx_ai_jobs_status ON ai_extraction_jobs(status);

