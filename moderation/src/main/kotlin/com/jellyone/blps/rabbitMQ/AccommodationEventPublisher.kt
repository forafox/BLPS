package com.jellyone.blps.service.rabbitMQ

import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessagePostProcessor
import org.springframework.stereotype.Service

@Service
class AccommodationEventPublisher(
    private val jmsTemplate: JmsTemplate
) {
    fun publishCreateEvent(ratingMessage: String) {
        print("try to send in moderation $ratingMessage")
        jmsTemplate.convertAndSend(
            "feedback.queue",
            ratingMessage,
            messagePostProcessor()
        )
    }

    private fun messagePostProcessor(): MessagePostProcessor {
        return MessagePostProcessor { message ->
            message.setStringProperty("_type", "createEvent")
            message
        }
    }

}