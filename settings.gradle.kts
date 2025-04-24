rootProject.name = "leader-board"

include("leader-board-game")

include("leader-board-score:leader-board-score-event-listener")
findProject(":leader-board-score:leader-board-score-event-listener")?.name = "leader-board-score-event-listener"

include("leader-board-score:leader-board-score-repository")
findProject(":leader-board-score:leader-board-score-repository")?.name = "leader-board-score-repository"

include("leader-board-score:leader-board-score-api")
findProject(":leader-board-score:leader-board-score-api")?.name = "leader-board-score-api"
