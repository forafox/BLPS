package com.jellyone.blps.service.rabbitMQ

import org.springframework.stereotype.Service
import org.springframework.jms.annotation.JmsListener;
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class AccommodationEventListener {

    @JmsListener(destination = "feedback.queue")
    fun handleEvent(event: JvmType.Object) {
        if (event.internalName.contains("success")) {
            println("Result of check: success")
        } else {
            println("Result of check: fail")
        }
    }
}