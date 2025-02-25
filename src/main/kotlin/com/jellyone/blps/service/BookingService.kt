package com.jellyone.blps.service

import com.jellyone.blps.domain.Booking
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.BookingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val accommodationService: AccommodationService,
    private val userService: UserService
) {
    fun create(
        arrivalDate: Date,
        departureDate: Date,
        questCount: Int,
        price: Int,
        accommodationId: Long,
        username: String
    ): Booking {
        val booking = Booking(
            id = 0,
            arrivalDate = arrivalDate,
            departureDate = departureDate,
            questCount = questCount,
            price = price,
            accommodation = accommodationService.getById(accommodationId),
            quest = userService.getByUsername(username)
        )
        return bookingRepository.save(booking)
    }

    fun getById(id: Long): Booking {
        return bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
    }

    fun update(
        id: Long,
        arrivalDate: Date,
        departureDate: Date,
        questCount: Int,
        price: Int,
    ): Booking {
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
        return bookingRepository.save(
            booking.copy(
                arrivalDate = arrivalDate,
                departureDate = departureDate,
                questCount = questCount,
                price = price,
            )
        )
    }

    fun delete(id: Long) {
        bookingRepository.deleteById(id)
    }
}