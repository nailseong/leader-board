package com.ilseong.game.service

import com.ilseong.game.message.GameEventProducer
import com.ilseong.game.repository.Game
import com.ilseong.game.repository.GameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val eventProducer: GameEventProducer,
) {

    @Transactional
    fun playGame(request: PlayRequest): PlayResponse {
        val winner = choiceWinner(request)
        val game = gameRepository.save(
            Game(
                leftPlayer = request.leftPlayer,
                rightPlayer = request.rightPlayer,
                winner = winner,
            )
        )

        val gameId = game.id
        checkNotNull(gameId) { "game id is null" }

        val response = PlayResponse(gameId, winner)
        eventProducer.sendGameEndEvent(response, game.createdAt ?: LocalDateTime.now())

        return response
    }

    private fun choiceWinner(request: PlayRequest): String {
        return listOf(request.leftPlayer, request.rightPlayer).random()
    }
}