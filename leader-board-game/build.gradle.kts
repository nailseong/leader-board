plugins {
    id("sub-module-build")
}

dependencies {
    // mysql
    implementation("mysql:mysql-connector-java:8.0.33")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:0.61.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.61.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.61.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.61.0")
    implementation("org.jetbrains.exposed:exposed-spring-boot-starter:0.61.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0") // kotlinx-datetime 의존성 추가

    // kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // monitoring
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.4")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus:1.14.6")
}
