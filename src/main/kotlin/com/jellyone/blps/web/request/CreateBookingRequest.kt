package com.jellyone.blps.web.request

import java.util.*

data class CreateBookingRequest(
    val arrivalDate: Date,
    val departureDate: Date,
    val guestCount: Int,
    val price: Int,
    val accommodationId: Long,
)

data class UpdateBookingRequest(
    val arrivalDate: Date,
    val departureDate: Date,
    val guestCount: Int,
    val price: Int,
)