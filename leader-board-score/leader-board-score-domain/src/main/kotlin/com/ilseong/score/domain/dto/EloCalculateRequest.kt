package com.ilseong.score.domain.dto

import java.time.LocalDateTime

data class EloCalculateRequest(
    val leftPlayer: String,
    val leftPlayerRating: Int,
    val rightPlayer: String,
    val rightPlayerRating: Int,
    val winner: String?,
    val date: LocalDateTime,
) {
    fun leftPlayerGameResult(): Double {
        return when {
            isDraw() -> 0.5
            winner == leftPlayer -> 1.0
            else -> 0.0
        }
    }

    fun rightPlayerGameResult(): Double {
        return when {
            isDraw() -> 0.5
            winner == rightPlayer -> 1.0
            else -> 0.0
        }
    }

    private fun isDraw(): Boolean {
        return winner == null
    }
}
