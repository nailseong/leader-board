rootProject.name = "leader-board"

include("leader-board-game")

include("leader-board-score:leader-board-score-event-listener")
findProject(":leader-board-score:leader-board-score-event-listener")?.name = "leader-board-score-event-listener"

include("leader-board-score:leader-board-score-repository")
findProject(":leader-board-score:leader-board-score-repository")?.name = "leader-board-score-repository"

include("leader-board-score:leader-board-score-api")
findProject(":leader-board-score:leader-board-score-api")?.name = "leader-board-score-api"

include("leader-board-score:leader-board-score-domain")
findProject(":leader-board-score:leader-board-score-domain")?.name = "leader-board-score-domain"

pluginManagement {

    plugins {
        kotlin("jvm") version "1.9.25"
        kotlin("plugin.spring") version "1.9.25"
        kotlin("plugin.jpa") version "1.9.25"
        id("org.springframework.boot") version "3.4.4"
        id("io.spring.dependency-management") version "1.1.7"
    }

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
