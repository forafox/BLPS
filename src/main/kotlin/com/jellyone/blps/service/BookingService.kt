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
        guestCount: Int,
        price: Int,
        accommodationId: Long,
        username: String
    ): Booking {
        checkAbilityToBooking(accommodationId, username)
        val booking = Booking(
            id = 0,
            arrivalDate = arrivalDate,
            departureDate = departureDate,
            guestCount = guestCount,
            price = price,
            accommodation = accommodationService.getById(accommodationId),
            guest = userService.getByUsername(username)
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
        guestCount: Int,
        price: Int,
        ownerUsername: String
    ): Booking {
        val owner = userService.getByUsername(ownerUsername)
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
        if (booking.guest != owner) throw AccessDeniedException("You do not have permission!")
        return bookingRepository.save(
            booking.copy(
                arrivalDate = arrivalDate,
                departureDate = departureDate,
                guestCount = guestCount,
                price = price,
            )
        )
    }

    fun delete(id: Long, ownerUsername: String) {
        val owner = userService.getByUsername(ownerUsername)
        val booking = bookingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Booking not found") }
        if (booking.guest != owner) throw AccessDeniedException("You do not have permission!")
        bookingRepository.deleteById(id)
    }

    private fun checkAbilityToBooking(accommodationId: Long, username: String) {
        val accommodation = accommodationService.getById(accommodationId)
        val user = userService.getByUsername(username)
        if (accommodation.owner.username == user.username) {
            throw AccessDeniedException("You do not have permission!")
        }
    }
}