package com.ilseong.game.service

import com.ilseong.game.repository.Game
import com.ilseong.game.repository.GameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
) {

    @Transactional
    fun playGame(request: PlayRequest): PlayResponse {
        val winner = choiceWinner(request)
        // todo : publish game end message

        val game = gameRepository.save(
            Game(
                leftPlayer = request.leftPlayer,
                rightPlayer = request.rightPlayer,
                winner = winner,
            )
        )

        val gameId = game.id
        checkNotNull(gameId) { "game id is null" }

        return PlayResponse(gameId, winner)
    }

    private fun choiceWinner(request: PlayRequest): String {
        return listOf(request.leftPlayer, request.rightPlayer).random()
    }
}