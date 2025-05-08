package com.ilseong.game.repository

import com.ilseong.game.service.PlayResponse
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class GameEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<GameEntity>(Games)

    var leftPlayer by Games.leftPlayer
    var rightPlayer by Games.rightPlayer
    var winner by Games.winner
    var isDraw by Games.isDraw
    var createdAt by Games.createdAt

    fun toResponse(): PlayResponse = PlayResponse(
        gameId = id.value,
        leftPlayer = leftPlayer,
        rightPlayer = rightPlayer,
        winner = winner,
        isDraw = isDraw
    )

    fun getJavaCreatedAt(): LocalDateTime =
        createdAt.toLocalDateTime(kotlinx.datetime.TimeZone.UTC).toJavaLocalDateTime()
}
