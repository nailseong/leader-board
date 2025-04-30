plugins {
    id("sub-module-build")
}

dependencies {
    implementation(project(":leader-board-score:leader-board-score-domain"))

    // kafka
    implementation("org.springframework.kafka:spring-kafka")
}
