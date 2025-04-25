package com.ilseong.score.message

import java.time.LocalDateTime

data class GameEndEvent(
    val winner: String?,
    val date: LocalDateTime,
)
