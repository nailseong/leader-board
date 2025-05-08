package com.ilseong.game.service

import com.ilseong.game.message.GameEventProducer
import com.ilseong.game.repository.GameEntity
import com.ilseong.game.repository.Games
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.stereotype.Service

@Service
class GameService(
    private val eventProducer: GameEventProducer,
) {

    companion object {
        private const val DRAW = "DRAW"
    }

    // DSL 방식
    suspend fun playGame(request: PlayRequest): PlayResponse = newSuspendedTransaction(Dispatchers.IO) {
        val winner = choiceWinner(request)

        val gameId = Games.insertAndGetId {
            it[leftPlayer] = request.leftPlayer
            it[rightPlayer] = request.rightPlayer
            it[Games.winner] = winner.takeIf { it != DRAW }
            it[isDraw] = winner == DRAW
        }

        val gameRow = Games.selectAll().where { Games.id eq gameId }.single()
        val createdAt = gameRow[Games.createdAt]
            .toLocalDateTime(kotlinx.datetime.TimeZone.UTC)
            .toJavaLocalDateTime()

        val response = PlayResponse(
            gameId = gameId.value,
            leftPlayer = gameRow[Games.leftPlayer],
            rightPlayer = gameRow[Games.rightPlayer],
            winner = gameRow[Games.winner],
            isDraw = gameRow[Games.isDraw]
        )

        eventProducer.sendGameEndEvent(response, createdAt)

        response
    }

    // DAO 방식
    suspend fun playGameByEntity(request: PlayRequest): PlayResponse = newSuspendedTransaction(Dispatchers.IO) {
        val winnerResult = choiceWinner(request)

        val entity = GameEntity.new {
            leftPlayer = request.leftPlayer
            rightPlayer = request.rightPlayer
            winner = winnerResult.takeIf { it != DRAW }
            isDraw = winnerResult == DRAW
            // createdAt은 자동으로 설정됨
        }

        val response = entity.toResponse()
        eventProducer.sendGameEndEvent(response, entity.getJavaCreatedAt())

        response
    }

    private fun choiceWinner(request: PlayRequest): String {
        return listOf(request.leftPlayer, request.rightPlayer, DRAW).random()
    }
}
