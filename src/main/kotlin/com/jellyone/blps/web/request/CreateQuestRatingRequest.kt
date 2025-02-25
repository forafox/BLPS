package com.jellyone.blps.web.request

import java.util.*

data class CreateQuestRatingRequest(
    val rating: Int,
    val feedback: String,
    val date: Date,
    val questId: Long,
    val bookingId: Long
)

data class UpdateQuestRatingRequest(
    val rating: Int,
    val feedback: String,
    val date: Date,
    val relevance: Boolean
)