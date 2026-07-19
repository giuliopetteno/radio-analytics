# Medical Equipment Analytics System (Radio Analytics)

A Kafka-based analytics microservice that builds and maintains a read-model derived from medical equipment lifecycle events, providing operational and business insights for the Radio ecosystem.

The service consumes domain events published by [`radio-registry`](https://github.com/giuliopetteno/radio-registry) and transforms them into an analytical read-model optimized for reporting and monitoring.

Application and business metrics are exposed via **Prometheus**, and **Grafana** is used for visualization and dashboards — the service does not expose its own REST API.

> **⚠️ Work in Progress**
>
> This project is currently under active development and serves as a demonstration of event-driven architecture and data pipeline practices in a modern Java backend context.
> New features, improvements, and additional integrations will be added over time.
>

## Features

- Read-model persistence for medical equipment lifecycle analytics
- Idempotent, event-driven architecture with deduplication and dead-letter handling
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
- Amazon Web Services (AWS) deployment:
  - EC2 (Docker Compose orchestration, IAM-only access via SSM)
  - ECR for container image registry
  - Automated CI/CD: GitHub Actions → OIDC → ECR → SSM Run Command deploy
  - Secrets management via AWS Systems Manager Parameter Store
- Gradle build system with Kotlin DSL
- Lombok for boilerplate code reduction

## Planned Enhancements

- Prometheus metrics exposure for application monitoring
- Grafana dashboards for data visualization
- Test suite: 
  - Unit tests (JUnit 5 & Mockito)
  - Slice tests (@WebMvcTest & @DataJpaTest)
  - Integration tests (@SpringBootTest & Testcontainers)
