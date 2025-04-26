package com.ilseong.score.controller

import com.ilseong.score.controller.dto.InitScoreRequest
import com.ilseong.score.service.RankingResponse
import com.ilseong.score.service.ScoreService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
class ScoreController(
    private val scoreService: ScoreService,
) {

    @GetMapping("/rankings")
    fun getTopRankings(
        @RequestParam(required = false) size: Long = 10,
        @RequestParam(required = false) date: LocalDateTime = LocalDateTime.now()
    ): RankingResponse {
        return scoreService.getTopRankings(size, date)
    }

    @PutMapping("/scores")
    fun initScore(@RequestBody request: InitScoreRequest) {
        scoreService.initScore(request)
    }
}