plugins {
	java
	id("org.springframework.boot") version "4.1.0"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "9.5.0"
}

group = "com.gp"
version = "0.0.1-SNAPSHOT"

val mockitoAgent: Configuration = configurations.create("mockitoAgent")

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("io.micrometer:micrometer-registry-prometheus")
	runtimeOnly("org.postgresql:postgresql")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation(platform("org.testcontainers:testcontainers-bom:2.0.5"))
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:testcontainers-junit-jupiter")
	testImplementation("org.testcontainers:testcontainers-postgresql")
	testImplementation("org.testcontainers:testcontainers-kafka")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	mockitoAgent("org.mockito:mockito-core:5.23.0") {
		isTransitive = false
	}
}

tasks.test {
	useJUnitPlatform()
	environment("PROFILE_ACTIVE", "test")
	jvmArgs("-javaagent:${mockitoAgent.asPath}", "-Xshare:off")
	filter {
		excludeTestsMatching("*IntegrationTest")
	}
}

springBoot {
	buildInfo()
}
