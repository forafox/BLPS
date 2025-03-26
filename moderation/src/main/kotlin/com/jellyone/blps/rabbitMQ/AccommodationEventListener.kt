package com.jellyone.blps.service.rabbitMQ

import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.jms.annotation.JmsListener;
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Slf4j
@Service
@RequiredArgsConstructor
class AccommodationEventListener(
    private val publisher: AccommodationEventPublisher
) {
    @JmsListener(destination = "accommodation.queue")
    fun handleEvent(event: JvmType.Object) {
        println("check in moderation service")
        if (event.internalName.contains("jsf")) {
            publisher.publishCreateEvent("fail")
        } else {
            publisher.publishCreateEvent("success")
        }
    }
}