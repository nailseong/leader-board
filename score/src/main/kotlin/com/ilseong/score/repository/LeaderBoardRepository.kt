package com.ilseong.score.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component

@Component
class LeaderBoardRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
) {

    companion object {
        private const val LEADER_BOARD_KEY = "LEADER_BOARD_KEY"
    }

    fun increaseScore(winner: String) {
        redisTemplate.opsForZSet()
            .incrementScore(LEADER_BOARD_KEY, winner, 1.0)
    }
}