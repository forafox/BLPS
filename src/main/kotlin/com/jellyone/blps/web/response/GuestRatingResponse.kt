package com.jellyone.blps.web.response

import com.jellyone.blps.domain.GuestRating

data class GuestRatingResponse(
    val id: Long,
    val rating: Int,
    val feedback: String,
    val date: String,
    val relevance: Boolean,
    val guest: UserResponse
)

fun GuestRating.toResponse() = GuestRatingResponse(
    id = id,
    rating = rating,
    feedback = feedback,
    date = date.toString(),
    relevance = relevance,
    guest = guest.toResponse()
)