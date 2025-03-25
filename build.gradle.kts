import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
    jacoco
}

fun removeVIfFirst(s: String) = if (s.startsWith("v")) s.removePrefix("v") else s

group = "com.jellyone"
version = removeVIfFirst(System.getenv("VERSION") ?: "latest")

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-config")
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.boot:spring-boot-starter-quartz")

    implementation("org.springframework.boot:spring-boot-starter-json")

    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("com.rabbitmq.jms:rabbitmq-jms:3.4.0")
    implementation("org.springframework:spring-jms")

//    implementation("com.rabbitmq.jms:rabbitmq-jms:2.4.0")
//    implementation("org.springframework.boot:spring-boot-starter-jms")
//    implementation("org.springframework:spring-jms:3.3.4")
//    implementation("com.rabbitmq.jms:rabbitmq-jms:2.4.0")
//    implementation("jakarta.jms:jakarta.jms-api:3.1.0")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")

    implementation("org.postgresql:postgresql:42.7.5")
    implementation("com.atomikos:transactions-spring-boot3-starter:6.0.0")
    implementation("org.springframework.security.kerberos:spring-security-kerberos-web:2.1.1")

    implementation("org.projectlombok:lombok:1.18.28")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.liquibase:liquibase-core")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")

    implementation("org.springframework.security.kerberos:spring-security-kerberos-client:1.0.1.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured:5.5.0")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootJar> {
    archiveFileName = "blps-lab1-v$version.jar"
}