plugins {
    id("sub-module-build")
    kotlin("plugin.jpa") version "1.9.25"
}

dependencies {
    // mysql
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("mysql:mysql-connector-java:8.0.33")

    // kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.4")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:1.14.6")
}
