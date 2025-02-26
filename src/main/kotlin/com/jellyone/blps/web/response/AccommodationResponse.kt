package com.jellyone.blps.web.response

import com.jellyone.blps.domain.Accommodation

data class AccommodationResponse(
    val id: Long,
    val country: String,
    val city: String,
    val address: String,
    val price: Int,
    val description: String,
    val owner: String
)

fun Accommodation.toResponse() = AccommodationResponse(
    id = id,
    country = country.name,
    city = city,
    address = address,
    price = price,
    description = description,
    owner = owner.username
)