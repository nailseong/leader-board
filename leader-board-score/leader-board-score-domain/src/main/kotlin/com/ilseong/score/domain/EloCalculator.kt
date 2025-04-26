package com.ilseong.score.domain

import com.ilseong.score.domain.dto.EloCalculateRequest
import com.ilseong.score.domain.dto.EloCalculateResponse
import org.springframework.stereotype.Component
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.roundToInt


@Component
class EloCalculator {

    companion object {
        private const val SIGMOID_SLOPE = 12.0
        private const val MIN_K_FACTOR = 10.0
        private const val MAX_K_FACTOR = 40.0
        private const val MEDIAN_EXPECTED_SCORE = 0.5
        private const val MIN_RATING = 100.0
    }

    /**
     * 경기 결과에 따라 두 플레이어의 레이팅을 업데이트합니다.
     *
     * @param player1Rating 플레이어1의 현재 레이팅
     * @param player2Rating 플레이어2의 현재 레이팅
     * @param result 1.0=플레이어1 승리, 0.0=플레이어2 승리, 0.5=무승부
     * @return Pair(player1NewRating, player2NewRating) 두 플레이어의 새로운 레이팅
     */
    fun updateRatings(request: EloCalculateRequest): EloCalculateResponse {
        return EloCalculateResponse(
            leftPlayerOldRating = request.leftPlayerRating,
            leftPlayerNewRating = calculateNewRating(
                playerRating = request.leftPlayerRating,
                opponentPlayerRating = request.rightPlayerRating,
                request.leftPlayerGameResult()
            ),
            rightPlayerOldRating = request.rightPlayerRating,
            rightPlayerNewRating = calculateNewRating(
                playerRating = request.rightPlayerRating,
                opponentPlayerRating = request.leftPlayerRating,
                request.rightPlayerGameResult()
            ),
        )
    }

    /**
     * 경기 결과에 따라 플레이어의 새로운 레이팅을 계산합니다.
     *
     * @param playerRating 플레이어의 현재 레이팅
     * @param opponentPlayerRating 상대 플레이어의 현재 레이팅
     * @param actualScore 실제 경기 결과 (승: 1.0, 패: 0.0, 무승부: 0.5)
     * @return 플레이어의 새로운 레이팅
     */
    private fun calculateNewRating(
        playerRating: Int,
        opponentPlayerRating: Int,
        actualScore: Double,
    ): Int {
        val expectedScore = calculateExpectedScore(playerRating, opponentPlayerRating)
//        val kFactor = calculateDynamicKFactor(expectedScore)
        val kFactor = calculateSigmoidKFactor(expectedScore)
        val newRating = playerRating + (kFactor * (actualScore - expectedScore))
        return maxOf(newRating, MIN_RATING).roundToInt()
    }

    /**
     * 플레이어 간의 기대 승률을 계산합니다.
     *
     * @param playerRating 플레이어의 현재 레이팅
     * @param opponentPlayerRating 상대 플레이어의 현재 레이팅
     * @return 플레이어의 기대 승률 (0에서 1 사이의 값)
     */
    private fun calculateExpectedScore(playerRating: Int, opponentPlayerRating: Int): Double {
        return 1.0 / (1.0 + 10.0.pow((opponentPlayerRating - playerRating) / 400.0))
    }

    /**
     * 기대 승률에 따라 규칙 기반으로 k factor 계산
     */
    private fun calculateDynamicKFactor(expectedScore: Double): Int {
        return when {
            expectedScore > 0.9 -> 10
            expectedScore > 0.7 -> 20
            expectedScore > 0.3 -> 30
            else -> 40
        }
    }

    /**
     * 기대 승률에 따라 sigmoid 함수를 활용해 k factor 계산
     */
    private fun calculateSigmoidKFactor(expectedScore: Double): Int {
        // Sigmoid 함수 - 기대 승률 0.5에서 중간값을 갖도록 조정
        // 1 - expectedScore를 사용하여 기대승률이 낮을수록 높은 K 값 적용
        val sigmoidValue = 1.0 / (1.0 + exp(SIGMOID_SLOPE * (expectedScore - MEDIAN_EXPECTED_SCORE)))

        // minK와 maxK 사이 값으로 매핑
        val kFactor = MIN_K_FACTOR + (MAX_K_FACTOR - MIN_K_FACTOR) * sigmoidValue

        return kFactor.roundToInt()
    }
}
