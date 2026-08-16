CREATE TYPE radio_analytics.analytics_status AS ENUM(
    'RUNNING',
    'COMPLETED',
    'PARTIAL',
    'FAILED'
);

CREATE TABLE IF NOT EXISTS radio_analytics.analytics_report(
    id BIGSERIAL PRIMARY KEY,
    report JSONB NOT NULL,
    report_status radio_analytics.analytics_status NOT NULL,
    generated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_analytics_report_generated_at ON radio_analytics.analytics_report(generated_at DESC);