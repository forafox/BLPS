package com.jellyone.blps.configuration

import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableRabbit
class RabbitConfig {

    @Bean
    fun accommodationQueue(): Queue {
        return Queue("accommodation.queue", true)
    }

    @Bean
    fun jsonMessageConverter() = Jackson2JsonMessageConverter()
}