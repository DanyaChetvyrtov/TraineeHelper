plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.core"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

dependencies {
	// boot starters
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// cloud starters
	implementation("org.springframework.cloud:spring-cloud-starter-config")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	// Minio
	implementation("io.minio:minio:8.5.17")

	// zipkin
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("io.zipkin.reporter2:zipkin-reporter-brave")

	// micrometr
	implementation("io.micrometer:micrometer-registry-prometheus")
	implementation("io.micrometer:micrometer-core")

	// swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")

	// db
	runtimeOnly("org.postgresql:postgresql")
	implementation("org.liquibase:liquibase-core")
	implementation("net.lbruun.springboot:preliquibase-spring-boot-starter:1.6.1")

	// mapper
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	// lombok
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok-mapstruct-binding:0.2.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
