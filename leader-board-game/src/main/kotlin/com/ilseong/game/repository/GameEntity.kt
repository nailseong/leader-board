package com.ilseong.game.repository

import com.ilseong.game.service.PlayResponse
import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.ResultRow
import java.time.LocalDateTime
import java.util.*

data class GameEntity(
    val id: UUID,
    val leftPlayer: String,
    val rightPlayer: String,
    val winner: String?,
    val isDraw: Boolean,
    val createdAt: Instant
) {
    companion object {
        fun fromRow(row: ResultRow): GameEntity = GameEntity(
            id = row[Games.id],
            leftPlayer = row[Games.leftPlayer],
            rightPlayer = row[Games.rightPlayer],
            winner = row[Games.winner],
            isDraw = row[Games.isDraw],
            createdAt = row[Games.createdAt]
        )
    }

    fun toResponse(): PlayResponse = PlayResponse(
        gameId = id.toString(),
        leftPlayer = leftPlayer,
        rightPlayer = rightPlayer,
        winner = winner,
        isDraw = isDraw
    )

    fun getJavaCreatedAt(): LocalDateTime =
        createdAt.toLocalDateTime(kotlinx.datetime.TimeZone.UTC).toJavaLocalDateTime()
}
