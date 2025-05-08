plugins {
    kotlin("jvm") apply false
    kotlin("plugin.spring") apply false
    id("org.springframework.boot") apply false
    id("io.spring.dependency-management") apply false
}

allprojects {
    group = "com.ilseong"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
