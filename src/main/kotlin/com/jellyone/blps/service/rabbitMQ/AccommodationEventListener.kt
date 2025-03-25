package com.jellyone.blps.service.rabbitMQ

import com.jellyone.blps.domain.Accommodation
import com.jellyone.blps.domain.CrudEvent
import org.springframework.stereotype.Service
import org.springframework.jms.annotation.JmsListener;
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Service
class AccommodationEventListener {

    @JmsListener(destination = "accommodation.queue")
    fun handleEvent(event: JvmType.Object) {
        println("Processing event: $event")
//        when (event.operation) {
//            "CREATE" -> processCreate(event.payload as Accommodation)
//            "UPDATE" -> processUpdate(event.payload as Accommodation)
//            "DELETE" -> processDelete(event.payload as Long)
//        }
    }

    private fun processCreate(accommodation: Accommodation) {
        println("Processing CREATE event for accommodation: ${accommodation.description}")
        // Логика обработки создания
    }

    private fun processUpdate(accommodation: Accommodation) {
        println("Processing UPDATE event for accommodation: ${accommodation.description}")
        // Логика обработки обновления
    }

    private fun processDelete(accommodationId: Long) {
        println("Processing DELETE event for accommodation ID: $accommodationId")
        // Логика обработки удаления
    }
}