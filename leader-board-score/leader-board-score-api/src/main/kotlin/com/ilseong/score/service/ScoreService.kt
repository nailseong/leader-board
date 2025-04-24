package com.ilseong.score.service

import com.ilseong.score.repository.LeaderBoardRepository
import org.springframework.stereotype.Service

@Service
class ScoreService(
    private val leaderBoardRepository: LeaderBoardRepository
) {

    fun increaseScore(winner: String) {
        leaderBoardRepository.increaseScore(winner)
    }

    fun getTopRankings(size: Long): RankingResponse {
        val rankings = leaderBoardRepository.getTopRankings(size)
        return RankingResponse.from(rankings)
    }
}
