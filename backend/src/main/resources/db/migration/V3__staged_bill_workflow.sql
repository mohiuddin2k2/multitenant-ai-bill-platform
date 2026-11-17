-- Add the clerk assignment link used by the staged bill approval workflow.
ALTER TABLE bills
    ADD COLUMN assigned_to_user_id BIGINT REFERENCES users(id) ON DELETE SET NULL;

CREATE INDEX idx_bills_assigned_to_user_id ON bills(assigned_to_user_id);
