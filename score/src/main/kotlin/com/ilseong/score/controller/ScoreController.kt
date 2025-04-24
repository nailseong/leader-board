package com.ilseong.score.controller

import com.ilseong.score.message.GameEndEvent
import com.ilseong.score.service.ScoreService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ScoreController(
    private val scoreService: ScoreService,
) {

    @PostMapping("/scores")
    fun increaseScore(@RequestBody request: GameEndEvent) {
        scoreService.increaseScore(request.winner)
    }
}