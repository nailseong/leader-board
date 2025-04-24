package com.ilseong.game.repository

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
data class Game(
    @Id
    val id: Long?,

    val leftPlayer: String,

    val rightPlayer: String,

    val winner: String?,

    @CreationTimestamp
    val createdAt: LocalDateTime?,
)
