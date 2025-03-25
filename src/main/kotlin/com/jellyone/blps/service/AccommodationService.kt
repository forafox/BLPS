package com.jellyone.blps.service

import com.jellyone.blps.domain.Accommodation
import com.jellyone.blps.domain.enums.Country
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.AccommodationRepository
import com.jellyone.blps.service.rabbitMQ.AccommodationEventPublisher
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service

@Service
class AccommodationService(
    private val accommodationRepository: AccommodationRepository,
    private val eventPublisher: AccommodationEventPublisher,
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

        val accommodationFromDB = accommodationRepository.save(accommodation)
        eventPublisher.publishCreateEvent(accommodationFromDB)
        return accommodationFromDB
    }

    fun getById(id: Long): Accommodation {
        return accommodationRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation not found") }
    }

    fun update(
        id: Long,
        country: Country,
        city: String,
        address: String,
        price: Int,
        description: String,
        ownerUsername: String
    ): Accommodation {
        val owner = userService.getByUsername(ownerUsername)
        val accommodation = accommodationRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation not found") }
        if (accommodation.owner != owner) throw AccessDeniedException("You do not have permission!")
        return accommodationRepository.save(
            accommodation.copy(
                country = country,
                city = city,
                address = address,
                price = price,
                description = description
            )
        )
    }

    fun delete(
        id: Long,
        ownerUsername: String
    ) {
        val owner = userService.getByUsername(ownerUsername)
        val accommodation = accommodationRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation not found") }
        if (accommodation.owner != owner) throw AccessDeniedException("You do not have permission!")
        accommodationRepository.deleteById(id)
    }
}