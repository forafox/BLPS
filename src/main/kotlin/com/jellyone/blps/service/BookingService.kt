package com.jellyone.blps.service

import com.jellyone.blps.domain.Booking
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.BookingRepository
import org.springframework.stereotype.Service
import java.nio.file.AccessDeniedException
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
        checkAbilityToBooking(accommodationId, username)
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
        ownerUsername: String
    ): Booking {
        val owner = userService.getByUsername(ownerUsername)
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
        if (booking.quest != owner) throw AccessDeniedException("You do not have permission!")
        return bookingRepository.save(
            booking.copy(
                arrivalDate = arrivalDate,
                departureDate = departureDate,
                questCount = questCount,
                price = price,
            )
        )
    }

    fun delete(id: Long, ownerUsername: String) {
        val owner = userService.getByUsername(ownerUsername)
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
        if (booking.quest != owner) throw AccessDeniedException("You do not have permission!")
        bookingRepository.deleteById(id)
    }

    private fun checkAbilityToBooking(accommodationId: Long, username: String) {
        val booking = bookingRepository.findById(accommodationId)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
        val user = userService.getByUsername(username)
        if (booking.quest.username == user.username) {
            throw AccessDeniedException("You do not have permission!")
        }
    }
}