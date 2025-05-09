package com.ilseong.game.service

import com.ilseong.game.message.GameEventProducer
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class GameService(
    private val eventProducer: GameEventProducer,
) {

    companion object {
        private const val DRAW = "DRAW"
    }

    suspend fun playGame(request: PlayRequest): PlayResponse = newSuspendedTransaction(Dispatchers.IO) {
        val winner = choiceWinner(request)

        val gameId = UUID.randomUUID().toString()
        val createdAt = LocalDateTime.now()

        val response = PlayResponse(
            gameId = gameId,
            leftPlayer = request.leftPlayer,
            rightPlayer = request.rightPlayer,
            winner = winner.takeIf { it != DRAW },
            isDraw = winner == DRAW,
        )

        eventProducer.sendGameEndEvent(response, createdAt)

        response
    }

    private fun choiceWinner(request: PlayRequest): String {
        return listOf(request.leftPlayer, request.rightPlayer, DRAW).random()
    }
}
