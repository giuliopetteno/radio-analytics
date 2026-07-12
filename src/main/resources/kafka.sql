CREATE TABLE IF NOT EXISTS processed_event(
    event_id UUID PRIMARY KEY,
    entity_type TEXT NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_processed_event_entity_type ON processed_event(entity_type);