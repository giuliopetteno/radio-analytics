# Medical Devices Analytics System (Radio Analytics)

Java microservice consuming **Apache Kafka** events that builds and maintains a read-model derived from medical device lifecycle events, providing operational and business insights for the Radio ecosystem.

The service consumes domain events published by [`radio-registry`](https://github.com/giuliopetteno/radio-registry) and transforms them into an analytical read-model optimized for reporting and monitoring.

Analytics are available both in real-time and through scheduled, persisted reports.

Application traces, metrics, and logs are exported via **OpenTelemetry** (OTLP) for consumption by the observability stack deployed in [`radio-infra`](https://github.com/giuliopetteno/radio-infra), where they are visualized in Grafana dashboards.

The microservice exposes internal REST endpoints for analytics consumption, accessible only within the Docker network and not exposed externally.

> **🚧 Work in Progress**
>
> This project is currently under active development and serves as a demonstration of event-driven architecture and data pipeline practices in a modern Java backend context.
> New features, improvements, and additional integrations will be added over time.

## Features

- Read-model persistence for real-time analytics and scheduled reporting across medical devices, organizational structure, lifecycle trends, and event activity
- RESTful APIs with layered architecture
- Exception handling
- Idempotent, event-driven architecture with deduplication and dead-letter handling
- Containerization
- Automated CI/CD pipeline
- Cloud deployment
- Full telemetry emission, designed for consumption by an external observability stack
- API documentation

## Technology Stack

- Java 26
- Spring Boot 4
- Hibernate / JPA
- PostgreSQL
- Apache Kafka for event-driven communication, with idempotent consumer and dead-letter handling
- Virtual Threads and Structured Concurrency for parallel task execution, deadlines, cancellation and partial degradation
- Environment-based configuration for default and production profiles
- Spring Boot Actuator for health, info and metrics endpoints, enabling production monitoring
- Containerization with Docker and Docker Compose
- Automated CI/CD with GitHub Actions
- Amazon Web Services (AWS) deployment:
  - EC2 (Docker Compose orchestration, IAM-only access via SSM)
  - ECR for container image registry
  - RDS (PostgreSQL, private subnet, EC2-scoped security group, SSM tunnel for local dev)
  - GitHub Actions → OIDC → ECR → SSM Run Command deploy
  - Secrets management via AWS Systems Manager Parameter Store
- OpenTelemetry (OTLP) integration for distributed tracing, metrics, and structured logging
- Gradle build system with Kotlin DSL
- Swagger / OpenAPI for interactive API documentation and endpoint testing
- Lombok for boilerplate code reduction

## Planned Enhancements

- Test suite: 
  - Unit tests (JUnit 5 and Mockito)
  - Slice tests (@WebMvcTest and @DataJpaTest)
  - Integration tests (@SpringBootTest and Testcontainers)
