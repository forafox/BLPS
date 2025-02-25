package com.jellyone.blps.web.request

import java.util.Date

data class CreateAccommodationRatingRequest(
    val overallImpression: Int,
    val putiry: Int,
    val accuracy: Int,
    val arrival: Int,
    val communication: Int,
    val location: Int,
    val priceQuality: Int,
    val convenience: Int,
    val feedback: String,
    val date: Date,
    val relevance: Boolean,
    val accommodationId: Long,
    val bookingId: Long
)