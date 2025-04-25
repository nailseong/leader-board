package com.ilseong.score.domain

import org.springframework.stereotype.Component
import kotlin.math.pow
import kotlin.math.roundToInt

@Component
class EloCalculator {

    companion object {
        private const val K_FACTOR = 32.0
        const val DEFAULT_INITIAL_RATING = 1200.0
    }

    /**
     * 경기 결과에 따라 두 플레이어의 레이팅을 업데이트합니다.
     *
     * @param player1Rating 플레이어1의 현재 레이팅
     * @param player2Rating 플레이어2의 현재 레이팅
     * @param result 1.0=플레이어1 승리, 0.0=플레이어2 승리, 0.5=무승부
     * @return Pair(player1NewRating, player2NewRating) 두 플레이어의 새로운 레이팅
     */
    fun updateRatings(
        player1Rating: Double,
        player2Rating: Double,
        result: Double,
    ): Pair<Int, Int> {
        val player1NewRating = calculateNewRating(player1Rating, player2Rating, result)
        val player2NewRating = calculateNewRating(player2Rating, player1Rating, 1.0 - result)

        return Pair(player1NewRating, player2NewRating)
    }

    /**
     * 경기 결과에 따라 플레이어의 새로운 레이팅을 계산합니다.
     *
     * @param playerRating 플레이어의 현재 레이팅
     * @param opponentRating 상대 플레이어의 현재 레이팅
     * @param actualScore 실제 경기 결과 (승: 1.0, 패: 0.0, 무승부: 0.5)
     * @return 플레이어의 새로운 레이팅
     */
    private fun calculateNewRating(
        playerRating: Double,
        opponentRating: Double,
        actualScore: Double,
    ): Int {
        val expectedScore = calculateExpectedScore(playerRating, opponentRating)
        return (playerRating + K_FACTOR * (actualScore - expectedScore)).roundToInt()
    }

    /**
     * 플레이어 간의 기대 승률을 계산합니다.
     *
     * @param playerRating 플레이어의 현재 레이팅
     * @param opponentRating 상대 플레이어의 현재 레이팅
     * @return 플레이어의 기대 승률 (0에서 1 사이의 값)
     */
    private fun calculateExpectedScore(playerRating: Double, opponentRating: Double): Double {
        return 1.0 / (1.0 + 10.0.pow((opponentRating - playerRating) / 400.0))
    }
}
