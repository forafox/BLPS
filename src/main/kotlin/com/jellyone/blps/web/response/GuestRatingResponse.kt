package com.jellyone.blps.web.response

import com.jellyone.blps.domain.QuestRating

data class GuestRatingResponse(
    val id: Long,
    val rating: Int,
    val feedback: String,
    val date: String,
    val relevance: Boolean,
    val quest: UserResponse
)

fun QuestRating.toResponse() = GuestRatingResponse(
    id = id,
    rating = rating,
    feedback = feedback,
    date = date.toString(),
    relevance = relevance,
    quest = quest.toResponse()
)