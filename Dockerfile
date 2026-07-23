FROM --platform=$BUILDPLATFORM gradle:9.5-jdk25 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties* ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon || return 0

COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

FROM --platform=$TARGETPLATFORM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S radio-analytics-group && adduser -S radio-analytics-user -G radio-analytics-group
USER radio-analytics-user

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]