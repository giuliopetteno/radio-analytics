CREATE TABLE IF NOT EXISTS radio_analytics.organization_event_log(
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    event_type TEXT NOT NULL,
    organization_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    code TEXT NOT NULL,
    description TEXT,
    organization_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    organization_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    produced_at TIMESTAMP WITH TIME ZONE NOT NULL,
    consumed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_organization_event_log_organization_id ON radio_analytics.organization_event_log(organization_id, produced_at);

CREATE TABLE IF NOT EXISTS radio_analytics.organization_snapshot(
    organization_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    code TEXT NOT NULL,
    description TEXT,
    organization_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    organization_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    last_event_id UUID NOT NULL,
    last_event_type TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS radio_analytics.department_event_log(
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    event_type TEXT NOT NULL,
    department_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    code TEXT NOT NULL,
    description TEXT,
    organization_id BIGINT,
    parent_department_id BIGINT,
    department_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    department_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    produced_at TIMESTAMP WITH TIME ZONE NOT NULL,
    consumed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_department_event_log_department_id ON radio_analytics.department_event_log(department_id, produced_at);

CREATE TABLE IF NOT EXISTS radio_analytics.department_snapshot(
    department_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    code TEXT NOT NULL,
    description TEXT,
    organization_id BIGINT,
    parent_department_id BIGINT,
    department_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    department_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    last_event_id UUID NOT NULL,
    last_event_type TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS radio_analytics.device_type_event_log(
    id BIGSERIAL PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    event_type TEXT NOT NULL,
    device_type_id BIGINT NOT NULL,
    name TEXT NOT NULL,
    description TEXT,
    device_type_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    device_type_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    produced_at TIMESTAMP WITH TIME ZONE NOT NULL,
    consumed_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_device_type_event_log_device_type_id ON radio_analytics.device_type_event_log(device_type_id, produced_at);

CREATE TABLE IF NOT EXISTS radio_analytics.device_type_snapshot(
    device_type_id BIGINT PRIMARY KEY,
    name TEXT NOT NULL,
    description TEXT,
    device_type_created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    device_type_updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    deleted_at TIMESTAMP WITH TIME ZONE,
    last_event_id UUID NOT NULL,
    last_event_type TEXT NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

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
    description TEXT,
    installation_date DATE NOT NULL,
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