package com.ilseong.score.domain.service

import com.ilseong.score.domain.EloCalculator
import com.ilseong.score.domain.dto.EloCalculateRequest
import com.ilseong.score.repository.LeaderBoardRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EloRatingService(
    private val leaderBoardRepository: LeaderBoardRepository,
    private val eloCalculator: EloCalculator,
) {

    companion object {
        private val logger = KotlinLogging.logger {}
        private const val START_RATING = 1200
    }

    fun updateEloRating(leftPlayer: String, rightPlayer: String, winner: String?, date: LocalDateTime) {
        val leftPlayerRating = leaderBoardRepository.findScore(leftPlayer, date)
        val rightPlayerRating = leaderBoardRepository.findScore(rightPlayer, date)

        val eloCalculateRequest = EloCalculateRequest(
            leftPlayer = leftPlayer,
            leftPlayerRating = leftPlayerRating ?: START_RATING,
            rightPlayer = rightPlayer,
            rightPlayerRating = rightPlayerRating ?: START_RATING,
            winner.takeIf { it != null },
            date
        )

        val eloCalculateResponse = eloCalculator.updateRatings(eloCalculateRequest)

        leaderBoardRepository.updateScore(leftPlayer, eloCalculateResponse.leftPlayerDiff(), date)
        logger.info { "Rating changed. $leftPlayer: $leftPlayerRating -> ${eloCalculateResponse.leftPlayerNewRating}" }

        leaderBoardRepository.updateScore(rightPlayer, eloCalculateResponse.rightPlayerDiff(), date)
        logger.info { "Rating changed. $rightPlayer: $rightPlayerRating -> ${eloCalculateResponse.rightPlayerNewRating}" }
    }
}