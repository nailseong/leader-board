package com.ilseong.score.controller

import com.ilseong.score.service.RankingResponse
import com.ilseong.score.service.ScoreService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ScoreController(
    private val scoreService: ScoreService,
) {

    @PostMapping("/scores")
    fun increaseScore(@RequestParam winner: String) {
        scoreService.increaseScore(winner)
    }

    @GetMapping("/rankings")
    fun getTopRankings(@RequestParam(required = false) size: Long = 10): RankingResponse {
        return scoreService.getTopRankings(size)
    }
}