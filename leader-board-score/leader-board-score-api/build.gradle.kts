plugins {
    id("sub-module-build")
}

dependencies {
    implementation(project(":leader-board-score:leader-board-score-domain"))
    implementation(project(":leader-board-score:leader-board-score-repository"))
}
