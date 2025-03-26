package com.jellyone.blps.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jms.annotation.EnableJms
import org.springframework.jms.config.DefaultJmsListenerContainerFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import com.rabbitmq.jms.admin.RMQConnectionFactory
import jakarta.jms.ConnectionFactory


@Configuration
@EnableJms
class JmsConfig {

    @Bean
    fun jmsConnectionFactory(): ConnectionFactory {
        return RMQConnectionFactory().apply {
            host = "localhost"
            port = 5672
            username = "guest"
            password = "guest"
        }
    }

    @Bean
    fun jacksonJmsMessageConverter(objectMapper: ObjectMapper): MessageConverter {
        return MappingJackson2MessageConverter().apply {
            setObjectMapper(objectMapper)
            setTargetType(MessageType.TEXT)
            setTypeIdPropertyName("_type")
        }
    }

    @Bean
    fun objectMapper(): ObjectMapper = JsonMapper.builder()
        .addModule(kotlinModule())
        .addModule(JavaTimeModule())
        .build()
        .apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

    @Bean
    fun jmsTemplate(connectionFactory: ConnectionFactory, messageConverter: MessageConverter): JmsTemplate {
        val template = JmsTemplate(connectionFactory)
        template.messageConverter = messageConverter
        return template
    }

    @Bean
    fun jmsListenerContainerFactory(connectionFactory: ConnectionFactory): DefaultJmsListenerContainerFactory {
        val factory = DefaultJmsListenerContainerFactory()
        factory.setConnectionFactory(connectionFactory)
        factory.setConcurrency("1-1")
        return factory
    }
}