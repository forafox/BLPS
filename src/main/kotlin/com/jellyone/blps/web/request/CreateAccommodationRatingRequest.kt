package com.jellyone.blps.web.request

import java.util.Date

data class CreateAccommodationRatingRequest(
    val overallImpression: Int,
    val purity: Int,
    val accuracy: Int,
    val arrival: Int,
    val communication: Int,
    val location: Int,
    val priceQuality: Int,
    val convenience: Int,
    val feedback: String,
    val privateFeedback: String,
    val date: Date,
    val accommodationId: Long,
    val bookingId: Long
)