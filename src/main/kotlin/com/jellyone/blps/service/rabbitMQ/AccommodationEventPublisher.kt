package com.jellyone.blps.service.rabbitMQ

import com.jellyone.blps.domain.Accommodation
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.core.MessagePostProcessor
import org.springframework.stereotype.Service

@Service
class AccommodationEventPublisher(
    private val jmsTemplate: JmsTemplate
) {
    fun publishCreateEvent(accommodation: Accommodation) {
        jmsTemplate.convertAndSend(
            "accommodation.queue",
            accommodation.description,
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