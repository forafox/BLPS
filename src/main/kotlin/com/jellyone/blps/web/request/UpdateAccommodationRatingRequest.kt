package com.jellyone.blps.web.request

import java.util.*

class UpdateAccommodationRatingRequest(
    val overallImpression: Int,
    val purity: Int,
    val accuracy: Int,
    val arrival: Int,
    val communication: Int,
    val location: Int,
    val priceQuality: Int,
    val convenience: Int,
    val feedback: String,
    val date: Date,
)