package com.ilseong.score

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ScoreApiApplication

fun main(args: Array<String>) {
    runApplication<ScoreApiApplication>(*args)
}