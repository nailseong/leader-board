package com.ilseong.score.service

import com.ilseong.score.repository.LeaderBoardRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScoreService(
    private val leaderBoardRepository: LeaderBoardRepository
) {

    fun getTopRankings(size: Long, date: LocalDateTime): RankingResponse {
        val rankings = leaderBoardRepository.getTopRankings(size, date)
        return RankingResponse.from(rankings)
    }
}
