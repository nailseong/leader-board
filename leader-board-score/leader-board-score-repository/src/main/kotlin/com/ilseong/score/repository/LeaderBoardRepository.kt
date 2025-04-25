package com.ilseong.score.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class LeaderBoardRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
) {

    companion object {
        private const val LEADER_BOARD_KEY_PREFIX = "LEADER_BOARD_"
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }

    fun increaseScore(winner: String, date: LocalDateTime) {
        redisTemplate.opsForZSet()
            .incrementScore(LEADER_BOARD_KEY_PREFIX + date.format(FORMATTER), winner, 1.0)
    }

    fun getTopRankings(size: Long, date: LocalDateTime): List<PlayerRanking> {
        return redisTemplate.opsForZSet()
            .reverseRangeWithScores(LEADER_BOARD_KEY_PREFIX + date.format(FORMATTER), 0, size - 1)
            ?.mapIndexed { index, typedTuple ->
                PlayerRanking(
                    ranking = index + 1,
                    player = typedTuple.value.toString(),
                    score = typedTuple.score?.toInt() ?: 0,
                )
            } ?: emptyList()
    }
}