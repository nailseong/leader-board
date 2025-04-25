package com.ilseong.game.message

import java.time.LocalDateTime

data class GameEndEvent(
    val winner: String,
    val date: LocalDateTime,
)
