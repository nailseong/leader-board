package com.ilseong.game.repository

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ExposedAutoConfiguration::class)
class ExposedConfig
