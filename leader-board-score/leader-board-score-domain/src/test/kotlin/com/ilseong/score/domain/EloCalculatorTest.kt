package com.ilseong.score.domain

import com.ilseong.score.domain.dto.EloCalculateRequest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EloCalculatorTest {

    private val eloCalculator: EloCalculator = EloCalculator()

    @Test
    fun `레이팅 계산`() {
        // given
        val request = EloCalculateRequest(
            "player_a",
            105,
            "player_b",
            102,
            "player_a",
            LocalDateTime.now()
        )

        // when
        val actual = eloCalculator.updateRatings(request)

        // then
        println("${actual.leftPlayerOldRating} -> ${actual.leftPlayerNewRating} (${actual.leftPlayerDiff()})")
        println("${actual.rightPlayerOldRating} -> ${actual.rightPlayerNewRating} (${actual.rightPlayerDiff()})")
//        found shouldNotBe null
//        found?.name shouldBe "테스트 사용자"
//        found?.email shouldBe "test@example.com"
    }
}