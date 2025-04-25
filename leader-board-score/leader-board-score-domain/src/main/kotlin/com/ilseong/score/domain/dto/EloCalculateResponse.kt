package com.ilseong.score.domain.dto

data class EloCalculateResponse(
    val leftPlayerNewRating: Int,
    val rightPlayerNewRating: Int,
)
