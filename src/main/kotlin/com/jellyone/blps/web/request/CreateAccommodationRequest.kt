package com.jellyone.blps.web.request

import com.jellyone.blps.domain.enums.Country

data class CreateAccommodationRequest(
    val country: Country,
    val city: String,
    val address: String,
    val price: Int,
    val description: String,
)