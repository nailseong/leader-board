package com.ilseong.game.service

import org.springframework.stereotype.Service

@Service
class GameService {

    fun playGame(request: PlayRequest): PlayResponse {
        val winner = choiceWinner(request)
        // todo : publish game end message
        // todo : save game record at db
        return PlayResponse(gameId = 1L, winner)
    }

    private fun choiceWinner(request: PlayRequest): String {
        return listOf(request.leftPlayer, request.rightPlayer).random()
    }
}