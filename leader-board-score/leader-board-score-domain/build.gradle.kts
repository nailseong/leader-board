plugins {
    id("sub-module-build")
}

tasks.bootJar { enabled = false }
tasks.jar { enabled = true }

dependencies {
    implementation(project(":leader-board-score:leader-board-score-repository"))
}
