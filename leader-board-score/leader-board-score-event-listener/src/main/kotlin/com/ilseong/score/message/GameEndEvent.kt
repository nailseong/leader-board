package com.ilseong.score.message

import java.time.LocalDateTime

data class GameEndEvent(
    val leftPlayer: String,
    val rightPlayer: String,
    val winner: String?,
    val isDraw: Boolean,
    val date: LocalDateTime,
)
