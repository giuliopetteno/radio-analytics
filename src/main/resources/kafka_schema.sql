CREATE TABLE IF NOT EXISTS radio_analytics.processed_event(
    event_id UUID PRIMARY KEY,
    entity_type TEXT NOT NULL,
    processed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_processed_event_entity_type ON radio_analytics.processed_event(entity_type);

CREATE TABLE IF NOT EXISTS radio_analytics.dead_letter_event(
     id BIGSERIAL PRIMARY KEY,
     original_topic TEXT NOT NULL,
     payload TEXT NOT NULL,
     error_message TEXT,
     error_class TEXT,
     resolved BOOLEAN NOT NULL DEFAULT FALSE,
     resolved_at TIMESTAMP WITH TIME ZONE,
     failed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_dead_letter_event_original_topic ON radio_analytics.dead_letter_event(original_topic, failed_at);