package com.ilseong.game.controller

import com.ilseong.game.service.GameService
import com.ilseong.game.service.PlayRequest
import com.ilseong.game.service.PlayResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(
    private val gameService: GameService
) {

    @PostMapping("/games")
    fun play(@RequestBody request: PlayRequest): PlayResponse {
        return gameService.playGame(request)
    }
}