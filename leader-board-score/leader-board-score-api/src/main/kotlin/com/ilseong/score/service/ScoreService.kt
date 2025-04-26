package com.ilseong.score.service

import com.ilseong.score.controller.dto.InitScoreRequest
import com.ilseong.score.domain.service.EloRatingService
import com.ilseong.score.repository.LeaderBoardRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ScoreService(
    private val leaderBoardRepository: LeaderBoardRepository,
    private val eloRatingService: EloRatingService,
) {

    fun getTopRankings(size: Long, date: LocalDateTime): RankingResponse {
        val rankings = leaderBoardRepository.getTopRankings(size, date)
        return RankingResponse.from(rankings)
    }

    fun initScore(request: InitScoreRequest) {
        eloRatingService.initEloRating(request.player, request.date ?: LocalDateTime.now())
    }
}
