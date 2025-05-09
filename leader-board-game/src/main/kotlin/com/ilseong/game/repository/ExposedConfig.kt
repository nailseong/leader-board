package com.ilseong.game.repository

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.ContextRefreshedEvent

@Configuration
@Import(ExposedAutoConfiguration::class)
class ExposedConfig : ApplicationListener<ContextRefreshedEvent> {

    @Value("\${exposed.drop.create:false}")
    private val dropCreate: Boolean = false

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        initTables()
    }


    fun initTables() {
        val tables = arrayOf(
            Games
        )

        if (dropCreate.not()) {
            return
        }

        transaction {
            SchemaUtils.drop(*tables)
            SchemaUtils.create(*tables)
        }
    }
}

