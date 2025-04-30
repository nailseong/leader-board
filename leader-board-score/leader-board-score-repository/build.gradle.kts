plugins {
    id("sub-module-build")
}

tasks.bootJar { enabled = false }
tasks.jar { enabled = true }

dependencies {
    // redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
}
