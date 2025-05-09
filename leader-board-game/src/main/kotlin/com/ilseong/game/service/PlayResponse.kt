package com.ilseong.game.service

data class PlayResponse(
    val gameId: String,
    val leftPlayer: String,
    val rightPlayer: String,
    val winner: String?,
    val isDraw: Boolean,
)
