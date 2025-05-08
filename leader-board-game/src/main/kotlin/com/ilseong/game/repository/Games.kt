package com.ilseong.game.repository

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

object Games : LongIdTable("game") {
    val leftPlayer = varchar("left_player", 255)
    val rightPlayer = varchar("right_player", 255)
    val winner = varchar("winner", 255).nullable()
    val isDraw = bool("is_draw")
    val createdAt = timestamp("created_at").clientDefault { Clock.System.now() }
}