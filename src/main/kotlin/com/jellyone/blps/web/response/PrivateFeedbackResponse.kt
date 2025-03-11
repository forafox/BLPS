package com.jellyone.blps.web.response

import com.jellyone.blps.domain.PrivateFeedback

data class PrivateFeedbackResponse (
    val id: Long,
    val feedback: String,
    val date: String,
    val owner: String,
    val booking: BookingResponse
)

fun PrivateFeedback.toResponse() = PrivateFeedbackResponse(
    id = id,
    feedback = feedback,
    date = date.toString(),
    owner = owner.username,
    booking = booking.toResponse()
)