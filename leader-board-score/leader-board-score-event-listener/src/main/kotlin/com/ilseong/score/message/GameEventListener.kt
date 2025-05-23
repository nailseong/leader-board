package com.ilseong.score.message

import com.fasterxml.jackson.databind.ObjectMapper
import com.ilseong.score.domain.service.EloRatingService
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service

@Service
class GameEventListener(
    private val objectMapper: ObjectMapper,
    private val eloRatingService: EloRatingService,
) {

    companion object {
        private const val TOPIC_GAME_RECORD_EVENT: String = "leader-board_game_result-event"
        private const val GROUP_ID_GAME_RECORD_EVENT = "leader-board_score"
        private val logger = KotlinLogging.logger {}
    }

    @KafkaListener(
        topics = [TOPIC_GAME_RECORD_EVENT],
        groupId = GROUP_ID_GAME_RECORD_EVENT,
        containerFactory = "kafkaListenerContainerFactory",
    )
    fun listen(
        @Header(KafkaHeaders.RECEIVED_KEY) gameId: String,
        @Payload eventString: String,
        ack: Acknowledgment
    ) {
        val event = objectMapper.readValue(eventString, GameEndEvent::class.java)
        logger.info { "Received GameEndEvent: $event, key: $gameId" }

        eloRatingService.updateEloRating(event.leftPlayer, event.rightPlayer, event.winner, event.date)

        ack.acknowledge()
    }
}
