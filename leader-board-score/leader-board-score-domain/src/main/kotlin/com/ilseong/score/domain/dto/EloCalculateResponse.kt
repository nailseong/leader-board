package com.ilseong.score.domain.dto

data class EloCalculateResponse(
    val leftPlayerOldRating: Int,
    val leftPlayerNewRating: Int,
    val rightPlayerOldRating: Int,
    val rightPlayerNewRating: Int,
) {
    fun leftPlayerDiff(): Int {
        return leftPlayerOldRating - leftPlayerNewRating
    }

    fun rightPlayerDiff(): Int {
        return rightPlayerOldRating - rightPlayerNewRating
    }
}
