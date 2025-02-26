package com.jellyone.blps.web.response

import com.jellyone.blps.domain.Booking

class BookingResponse(
    val id: Long,
    val arrivalDay: String,
    val departureDay: String,
    val guestCount: Int,
    val price: Int,
    val quest: UserResponse,
    val accommodation: AccommodationResponse
)

fun Booking.toResponse() = BookingResponse(
    id = id,
    arrivalDay = arrivalDate.toString(),
    departureDay = departureDate.toString(),
    guestCount = questCount,
    price = price,
    quest = quest.toResponse(),
    accommodation = accommodation.toResponse()
)