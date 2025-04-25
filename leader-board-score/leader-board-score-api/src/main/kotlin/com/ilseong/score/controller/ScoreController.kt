package com.ilseong.score.controller

import com.ilseong.score.service.RankingResponse
import com.ilseong.score.service.ScoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class ScoreController(
    private val scoreService: ScoreService,
) {

    @PostMapping("/scores")
    fun increaseScore(
        @RequestParam winner: String,
        @RequestParam(required = false) date: LocalDateTime = LocalDateTime.now()
    ) {
        scoreService.increaseScore(winner, date)
    }

    @GetMapping("/rankings")
    fun getTopRankings(
        @RequestParam(required = false) size: Long = 10,
        @RequestParam(required = false) date: LocalDateTime = LocalDateTime.now()
    ): RankingResponse {
        return scoreService.getTopRankings(size, date)
    }
}