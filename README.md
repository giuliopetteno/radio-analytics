# Medical Equipment Analytics System (Radio Analytics)

A Kafka-based analytics microservice that builds and maintains a read-model derived from medical equipment lifecycle events, providing operational and business insights for the Radio Registry ecosystem.

The service consumes domain events published by Radio Registry (device creation, relocation, and decommissioning) and transforms them into an analytical read-model optimized for reporting and monitoring, without exposing its own REST API — visualization is delegated to Grafana.

> **⚠️ Work in Progress**
>
> This project is currently under active development and serves as a demonstration of event-driven architecture and data pipeline practices in a modern Java backend context.
> New features, improvements, and additional integrations will be added over time.
