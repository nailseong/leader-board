package com.ilseong.game.repository

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
data class Game(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val leftPlayer: String,

    val rightPlayer: String,

    val winner: String? = null,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
)
