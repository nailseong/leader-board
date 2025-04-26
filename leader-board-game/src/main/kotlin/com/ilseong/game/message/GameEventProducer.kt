package com.ilseong.game.message

import com.ilseong.game.service.PlayResponse
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Service
class GameEventProducer(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) {

    companion object {
        private const val TOPIC: String = "game-record-event"
        private val logger = KotlinLogging.logger {}
    }

    fun sendGameEndEvent(playResponse: PlayResponse, date: LocalDateTime): CompletableFuture<SendResult<String, Any>> {
        val event = GameEndEvent(
            playResponse.leftPlayer,
            playResponse.rightPlayer,
            playResponse.winner,
            playResponse.isDraw,
            date
        )
        return kafkaTemplate.send(TOPIC, playResponse.gameId.toString(), event)
            .whenComplete { result, ex ->
                if (ex == null) {
                    logger.info { "이벤트 전송 성공: ${result.recordMetadata.topic()}-${result.recordMetadata.partition()}, 오프셋: ${result.recordMetadata.offset()}" }
                } else {
                    logger.error(ex) { "이벤트 전송 실패" }
                }
            }
    }
}