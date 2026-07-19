CREATE TABLE IF NOT EXISTS radio_analytics.device_event_log(
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    event_type TEXT NOT NULL,
    device_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    device_type_id BIGINT NOT NULL,
    serial_number TEXT NOT NULL,
    description TEXT,
    installation_date DATE NOT NULL,
    device_status TEXT NOT NULL,
    decommission_date DATE,
    organization_id BIGINT,
    department_id BIGINT,
    device_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    device_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    produced_at TIMESTAMP WITH TIME ZONE NOT NULL,
    consumed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_device_event_log_device_id ON radio_analytics.device_event_log(device_id, produced_at);

CREATE TABLE IF NOT EXISTS radio_analytics.device_snapshot(
    device_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    device_type_id BIGINT NOT NULL,
    serial_number TEXT NOT NULL,
    device_status TEXT NOT NULL,
    decommission_date DATE,
    organization_id BIGINT,
    department_id BIGINT,
    device_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    device_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    last_event_id UUID NOT NULL,
    last_event_type TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);