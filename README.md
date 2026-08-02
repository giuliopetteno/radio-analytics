# Medical Devices Analytics System (Radio Analytics)

Java microservice consuming Apache Kafka events that builds and maintains a read-model derived from medical devices lifecycle events, providing observability alongside operational and business insights for the Radio ecosystem.

The service consumes domain events published by [`radio-registry`](https://github.com/giuliopetteno/radio-registry) and transforms them into an analytical read-model optimized for reporting and monitoring.

Application traces, metrics, and logs are exported via **OpenTelemetry** (OTLP), collected by **Alloy** and stored in **Prometheus** (metrics), **Tempo** (distributed traces), and **Loki** (logs).
**Grafana** ties the three signals together for visualization, dashboards, and trace-to-log correlation.
The service does not expose its own REST API.

> **🚧 Work in Progress**
>
> This project is currently under active development and serves as a demonstration of event-driven architecture and data pipeline practices in a modern Java backend context.
> New features, improvements, and additional integrations will be added over time.

## Live Demo

Grafana dashboards are available at:
[radio-analytics.giuliopetteno.dev](https://giuliopetteno.s.gy/radio-analytics)

> **Note:** Anonymous read-only access — no login required.

## Features

- Read-model persistence for medical devices lifecycle analytics
- Idempotent, event-driven architecture with deduplication and dead-letter handling
- Full observability stack: distributed tracing, metrics, and structured logging with cross-signal correlation
- Exception handling
- Layered architecture following enterprise development practices
- Containerization
- Automated CI/CD pipeline
- Cloud deployment

## Technology Stack

- Java 25
- Spring Boot 4
- Spring Boot Actuator for health, info & metrics endpoints, enabling production monitoring
- Hibernate / JPA
- PostgreSQL
- Apache Kafka Idempotent Consumer for event-driven communication
- Environment-based configuration for default & production profiles
- Containerization with Docker & Docker Compose
- Observability stack:
  - Alloy as unified OpenTelemetry collector
  - Prometheus for metrics storage
  - Tempo for distributed trace storage
  - Loki for log aggregation
  - Grafana for dashboards, visualization, and trace-to-log correlation
- Amazon Web Services (AWS) deployment:
  - EC2 (Docker Compose orchestration, IAM-only access via SSM)
  - ECR for container image registry
  - Automated CI/CD: GitHub Actions → OIDC → ECR → SSM Run Command deploy
  - Secrets management via AWS Systems Manager Parameter Store
  - Nginx reverse proxy for name-based routing, with TLS via Let's Encrypt and automated renewal
- Gradle build system with Kotlin DSL
- Lombok for boilerplate code reduction

## Planned Enhancements

- Test suite: 
  - Unit tests (JUnit 5 & Mockito)
  - Slice tests (@WebMvcTest & @DataJpaTest)
  - Integration tests (@SpringBootTest & Testcontainers)
