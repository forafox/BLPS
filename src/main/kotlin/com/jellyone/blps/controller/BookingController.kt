package com.jellyone.blps.controller

import com.jellyone.blps.service.BookingService
import com.jellyone.blps.web.request.CreateBookingRequest
import com.jellyone.blps.web.request.UpdateBookingRequest
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
        response.questCount,
        response.price,
        response.accommodationId,
        principal.name
    )

    @GetMapping("/{id}")
    fun getBookingById(@PathVariable id: Long) =
        bookingService.getById(id)

    @PutMapping("/{id}")
    fun updateBookingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateBookingRequest,
        principal: Principal
    ) = bookingService.update(
        id,
        response.arrivalDate,
        response.departureDate,
        response.questCount,
        response.price,
        principal.name
    )

    @DeleteMapping("/{id}")
    fun deleteBookingById(@PathVariable id: Long, principal: Principal) = bookingService.delete(id, principal.name)
}