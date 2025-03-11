package com.jellyone.blps.web.request

import java.util.*

data class CreateGuestRatingRequest(
    val rating: Int,
    val feedback: String,
    val date: Date,
    val guestId: Long,
    val bookingId: Long
)

data class UpdateGuestRatingRequest(
    val rating: Int,
    val feedback: String,
    val date: Date,
    val relevance: Boolean
)