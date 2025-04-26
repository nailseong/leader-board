package com.ilseong.score.controller.dto

import java.time.LocalDateTime

data class InitScoreRequest(
    val player: String,
    val date: LocalDateTime?
)
