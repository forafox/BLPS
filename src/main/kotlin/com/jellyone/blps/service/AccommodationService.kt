package com.jellyone.blps.service

import com.jellyone.blps.domain.Accommodation
import com.jellyone.blps.domain.enums.Country
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.AccommodationRepository
import org.springframework.stereotype.Service

@Service
class AccommodationService(
    private val accommodationRepository: AccommodationRepository,
    private val userService: UserService
) {
    fun create(
        country: Country,
        city: String,
        address: String,
        price: Int,
        description: String,
        ownerUsername: String
    ): Accommodation {
        val user = userService.getByUsername(ownerUsername)
        val accommodation = Accommodation(
            id = 0,
            country = country,
            city = city,
            address = address,
            price = price,
            description = description,
            owner = user
        )
        return accommodationRepository.save(accommodation)
    }

    fun getById(id: Long): Accommodation {
        return accommodationRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation not found") }
    }
}