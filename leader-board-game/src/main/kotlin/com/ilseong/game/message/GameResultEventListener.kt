package com.ilseong.game.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.ilseong.game.repository.Games
import mu.KotlinLogging
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import java.util.*

@Service
class GameResultEventListener(
    private val objectMapper: ObjectMapper,
) {

    companion object {
        private const val GROUP_ID_GAME_RECORD_EVENT = "leader-board_game"
        private val logger = KotlinLogging.logger {}
    }

    @KafkaListener(
        topics = [GameEventProducer.GAME_RECORD_EVENT_TOPIC],
        groupId = GROUP_ID_GAME_RECORD_EVENT,
        containerFactory = "kafkaListenerContainerFactory",
    )
    suspend fun listen(
        @Header(KafkaHeaders.RECEIVED_KEY) gameId: String,
        @Payload eventString: String,
        ack: Acknowledgment
    ) {
        val event = objectMapper.readValue(eventString, GameEndEvent::class.java)
        UUID.fromString(gameId)

        newSuspendedTransaction {
            Games.insert {
                it[id] = UUID.fromString(gameId)
                it[leftPlayer] = event.leftPlayer
                it[rightPlayer] = event.rightPlayer
                it[winner] = event.winner
                it[isDraw] = event.isDraw
            }
        }

        logger.info { "Received GameEndEvent: $event, key: $gameId" }

        ack.acknowledge()
    }
}