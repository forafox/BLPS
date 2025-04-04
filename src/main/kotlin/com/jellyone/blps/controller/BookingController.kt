package com.jellyone.blps.controller

import com.jellyone.blps.service.BookingService
import com.jellyone.blps.web.request.CreateBookingRequest
import com.jellyone.blps.web.request.UpdateBookingRequest
import com.jellyone.blps.web.response.toResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Bookings API")
@SecurityRequirement(name = "JWT")
class BookingController(
    private val bookingService: BookingService
) {
    @PostMapping("")
    fun createBooking(
        @RequestBody response: CreateBookingRequest,
        principal: Principal
    ) = bookingService.create(
        response.arrivalDate,
        response.departureDate,
        response.guestCount,
        response.price,
        response.accommodationId,
        principal.name
    ).toResponse()

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable id: Long) =
        bookingService.getById(id).toResponse()

    @PutMapping("/{id}")
    fun updateBookingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateBookingRequest,
        principal: Principal
    ) = bookingService.update(
        id,
        response.arrivalDate,
        response.departureDate,
        response.guestCount,
        response.price,
        principal.name
    ).toResponse()

    @DeleteMapping("/{id}")
    fun deleteBookingById(@PathVariable id: Long, principal: Principal) = bookingService.delete(id, principal.name)
}