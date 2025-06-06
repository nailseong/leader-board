package com.ilseong.score.repository

import mu.KotlinLogging
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
        private val logger = KotlinLogging.logger {}

        private fun key(date: LocalDateTime) = LEADER_BOARD_KEY_PREFIX + date.format(FORMATTER)
    }

    fun setScore(player: String, initialRating: Int, date: LocalDateTime) {
        redisTemplate.opsForZSet()
            .add(key(date), player, initialRating.toDouble())
    }

    fun findScore(player: String, date: LocalDateTime): Int? {
        return redisTemplate.opsForZSet().score(key(date), player)?.toInt()
    }

    fun updateScore(player: String, scoreDiff: Int, date: LocalDateTime) {
        redisTemplate.opsForZSet()
            .incrementScore(key(date), player, scoreDiff.toDouble())
    }

    fun getTopRankings(size: Long, date: LocalDateTime): List<PlayerRanking> {
        return redisTemplate.opsForZSet()
            .reverseRangeWithScores(key(date), 0, size - 1)
            ?.mapIndexed { index, typedTuple ->
                PlayerRanking(
                    ranking = index + 1,
                    player = typedTuple.value.toString(),
                    score = typedTuple.score?.toInt() ?: 0,
                )
            } ?: emptyList()
    }
}