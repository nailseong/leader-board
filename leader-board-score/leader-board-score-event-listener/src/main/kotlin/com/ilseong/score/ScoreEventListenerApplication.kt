package com.ilseong.score

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScoreEventListenerApplication

fun main(args: Array<String>) {
    runApplication<ScoreEventListenerApplication>(*args)
}