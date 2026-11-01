-- V2__seed_demo_data.sql
-- Seed Initial Multi-Tenant Organizations, Users, Quotas, and Bills

-- 1. Insert Organizations (Parent / Sub-Org Hierarchy)
INSERT INTO organizations (id, name, code, parent_id, org_path, created_at, updated_at) VALUES
(1, 'Shaksham Global Enterprises', 'SHAKSHAM-HQ', NULL, '/1/', NOW(), NOW()),
(2, 'Shaksham North Division', 'SHAKSHAM-NORTH', 1, '/1/2/', NOW(), NOW()),
(3, 'Shaksham South Operations', 'SHAKSHAM-SOUTH', 1, '/1/3/', NOW(), NOW());

SELECT setval('organizations_id_seq', 3);

-- 2. Insert Tenant Quotas
INSERT INTO tenant_quotas (id, tenant_id, monthly_token_limit, tokens_used_this_month, monthly_cost_cap_usd, cost_accumulated_usd, last_reset_date, is_throttled) VALUES
(1, 1, 5000000, 142000, 150.00, 21.30, CURRENT_DATE, FALSE),
(2, 2, 2000000, 85000, 50.00, 12.75, CURRENT_DATE, FALSE),
(3, 3, 2000000, 42000, 50.00, 6.30, CURRENT_DATE, FALSE);

SELECT setval('tenant_quotas_id_seq', 3);

-- 3. Insert Users (Password: password123 for all users)
-- Password hash: $2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm
INSERT INTO users (id, tenant_id, username, email, password_hash, full_name, role, enabled, created_at) VALUES
-- HQ Admins
(1, 1, 'hq_admin', 'admin@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Rajesh Sharma (HQ Admin)', 'ADMIN', TRUE, NOW()),

-- North Division Users
(2, 2, 'north_admin', 'north_admin@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Priya Kapoor (North Admin)', 'ADMIN', TRUE, NOW()),
(3, 2, 'north_clerk', 'clerk_north@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Amit Verma (North Clerk)', 'CLERK', TRUE, NOW()),
(4, 2, 'north_approver', 'approver_north@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Suresh Nair (North Approver)', 'APPROVER', TRUE, NOW()),

-- South Operations Users
(5, 3, 'south_admin', 'south_admin@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Deepa Iyer (South Admin)', 'ADMIN', TRUE, NOW()),
(6, 3, 'south_clerk', 'clerk_south@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Vikram Rao (South Clerk)', 'CLERK', TRUE, NOW()),
(7, 3, 'south_approver', 'approver_south@shaksham.com', '$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxGQrvkWGyWm', 'Ananya Reddy (South Approver)', 'APPROVER', TRUE, NOW());

SELECT setval('users_id_seq', 7);

-- 4. Seed Representative Bills for North Division (Tenant 2)
INSERT INTO bills (id, tenant_id, submitted_by_user_id, invoice_number, vendor_name, vendor_tax_id, category, bill_date, due_date, currency, subtotal_amount, tax_amount, total_amount, status, notes, is_anomaly, anomaly_severity, anomaly_reasons, ai_confidence_score, created_at, updated_at) VALUES
(1, 2, 3, 'INV-2026-0891', 'Tata Consultancy Services', '27AAACT2727Q1ZW', 'IT Services', CURRENT_DATE - INTERVAL '12 days', CURRENT_DATE + INTERVAL '18 days', 'INR', 125000.00, 22500.00, 147500.00, 'APPROVED', 'Annual software support license renewal', FALSE, NULL, NULL, 98.5, NOW(), NOW()),
(2, 2, 3, 'INV-2026-1044', 'Reliance Power Ltd', '07AABCR4567A1Z2', 'Utilities', CURRENT_DATE - INTERVAL '5 days', CURRENT_DATE + INTERVAL '25 days', 'INR', 68400.00, 12312.00, 80712.00, 'PENDING', 'Monthly data center electricity bill', FALSE, NULL, NULL, 99.1, NOW(), NOW()),
(3, 2, 3, 'INV-2026-2100', 'Office Depot Logistics', '29AABCO1234B1Z5', 'Supplies', CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '15 days', 'INR', 345000.00, 62100.00, 407100.00, 'IN_REVIEW', 'Bulk ergonomic workstations for expanded floor', TRUE, 'HIGH', 'Amount is 4.8x higher than average vendor ticket size; unusually high line item count', 92.4, NOW(), NOW()),
(4, 2, 3, 'INV-2026-1044', 'Reliance Power Ltd', '07AABCR4567A1Z2', 'Utilities', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE + INTERVAL '29 days', 'INR', 68400.00, 12312.00, 80712.00, 'REJECTED', 'Duplicate submission rejected by auto-scan', TRUE, 'CRITICAL', 'Duplicate invoice detected: Same invoice number (INV-2026-1044) and vendor already submitted on same cycle', 99.8, NOW(), NOW()),
(5, 2, 3, 'INV-2026-5520', 'Blue Dart Express', '33AABCB1122C1Z8', 'Logistics', CURRENT_DATE - INTERVAL '3 days', CURRENT_DATE + INTERVAL '12 days', 'INR', 14200.00, 2556.00, 16756.00, 'PENDING', 'Freight logistics for hardware shipment', FALSE, NULL, NULL, 96.0, NOW(), NOW()),
(6, 2, 3, 'INV-2026-9021', 'Amazon Web Services Cloud', '9917USA29005088', 'Cloud Infrastructure', CURRENT_DATE - INTERVAL '15 days', CURRENT_DATE + INTERVAL '5 days', 'INR', 210000.00, 37800.00, 247800.00, 'PAID', 'Monthly production Kubernetes cluster hosting', FALSE, NULL, NULL, 99.4, NOW(), NOW());

SELECT setval('bills_id_seq', 6);

-- 5. Seed Bill Items for North Division Bills
INSERT INTO bill_items (id, bill_id, tenant_id, description, quantity, unit_price, total_price) VALUES
(1, 1, 2, 'Enterprise Support Package - Annual Tier 2', 1, 125000.00, 125000.00),
(2, 2, 2, 'Substation Meter Reading Units (34,200 kWh)', 34200, 2.00, 68400.00),
(3, 3, 2, 'Ergonomic Desk & Chair Sets (Executive)', 50, 6900.00, 345000.00),
(4, 4, 2, 'Substation Meter Reading Units (Duplicate)', 34200, 2.00, 68400.00),
(5, 5, 2, 'Surface Cargo Priority Dispatch - 18 Crates', 18, 788.89, 14200.00),
(6, 6, 2, 'EKS Production Clusters + S3 Storage Storage', 1, 210000.00, 210000.00);

SELECT setval('bill_items_id_seq', 6);

-- 6. Seed Representative Bills for South Operations (Tenant 3)
INSERT INTO bills (id, tenant_id, submitted_by_user_id, invoice_number, vendor_name, vendor_tax_id, category, bill_date, due_date, currency, subtotal_amount, tax_amount, total_amount, status, notes, is_anomaly, anomaly_severity, anomaly_reasons, ai_confidence_score, created_at, updated_at) VALUES
(7, 3, 6, 'INV-SOUTH-701', 'Airtel Business Fiber', '07AABCA1234D1Z9', 'Utilities', CURRENT_DATE - INTERVAL '4 days', CURRENT_DATE + INTERVAL '20 days', 'INR', 32000.00, 5760.00, 37760.00, 'PENDING', 'Dedicated leased line internet 500Mbps', FALSE, NULL, NULL, 97.2, NOW(), NOW()),
(8, 3, 6, 'INV-SOUTH-805', 'Godrej Facilities Mgmt', '27AABCG5678E1Z1', 'Facilities', CURRENT_DATE - INTERVAL '8 days', CURRENT_DATE + INTERVAL '10 days', 'INR', 95000.00, 17100.00, 112100.00, 'APPROVED', 'Commercial office security and sanitation services', FALSE, NULL, NULL, 95.8, NOW(), NOW()),
(9, 3, 6, 'INV-SOUTH-912', 'Dell Technologies India', '29AABCD9999F1Z3', 'IT Equipment', CURRENT_DATE - INTERVAL '1 day', CURRENT_DATE + INTERVAL '30 days', 'INR', 850000.00, 153000.00, 1003000.00, 'PENDING', 'Server rack upgrade for Chennai warehouse hub', TRUE, 'HIGH', 'Bill exceeds standard approval threshold of 500,000 INR; requires dual sign-off', 94.0, NOW(), NOW());

SELECT setval('bills_id_seq', 9);

-- 7. Seed Bill Items for South Operations
INSERT INTO bill_items (id, bill_id, tenant_id, description, quantity, unit_price, total_price) VALUES
(7, 7, 3, 'Primary ILL 500Mbps Leased Line Monthly', 1, 32000.00, 32000.00),
(8, 8, 3, 'Facility Operations Staffing (15 Personnel)', 1, 95000.00, 95000.00),
(9, 9, 3, 'Dell PowerEdge R750 Rack Servers', 2, 425000.00, 850000.00);

SELECT setval('bill_items_id_seq', 9);

-- 8. Seed Audit Logs
INSERT INTO bill_audit_logs (bill_id, tenant_id, action, performed_by_user_id, comment, timestamp) VALUES
(1, 2, 'CREATED', 3, 'Initial upload and auto-extraction completed', NOW() - INTERVAL '12 days'),
(1, 2, 'APPROVED', 4, 'Verified contract terms and approved payment', NOW() - INTERVAL '10 days'),
(3, 2, 'CREATED', 3, 'Uploaded bulk invoice for workstation procurement', NOW() - INTERVAL '2 days'),
(4, 2, 'REJECTED', 4, 'Flagged as duplicate submission against INV-2026-1044', NOW() - INTERVAL '1 day');

