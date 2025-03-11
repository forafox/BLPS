package com.jellyone.blps.web.response

import com.jellyone.blps.domain.AccommodationRating

data class AccommodationRatingResponse(
    val id: Long,
    val rating: Int,
    val feedback: String,
    val date: String,
    val relevance: Boolean,
    val accommodation: AccommodationResponse,
    val booking: BookingResponse
)

fun AccommodationRating.toResponse() = AccommodationRatingResponse(
    id = id,
    rating = calculateRating(
        overallImpression,
        purity,
        accuracy,
        arrival,
        communication,
        location,
        priceQuality,
    ),
    feedback = feedback,
    date = date.toString(),
    relevance = relevance,
    accommodation = accommodation.toResponse(),
    booking = booking.toResponse()
)

private fun calculateRating(
    overallImpression: Int,
    purity: Int,
    accuracy: Int,
    arrival: Int,
    communication: Int,
    location: Int,
    priceQuality: Int
): Int {
    return (overallImpression + purity + accuracy + arrival + communication + location + priceQuality) / 7
}