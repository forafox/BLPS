package com.jellyone.blps.service

import com.jellyone.blps.domain.AccommodationRating
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.AccommodationRatingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AccommodationRatingService(
    private val accommodationRatingRepository: AccommodationRatingRepository,
    private val accommodationService: AccommodationService,
    private val bookingService: BookingService
) {
    fun create(
        overallImpression: Int,
        putiry: Int,
        accuracy: Int,
        arrival: Int,
        communication: Int,
        location: Int,
        priceQuality: Int,
        convenience: Int,
        feedback: String,
        date: Date,
        relevance: Boolean,
        accommodationId: Long,
        bookingId: Long
    ): AccommodationRating {
        val accommodationRating = AccommodationRating(
            id = 0,
            overallImpression = overallImpression,
            putiry = putiry,
            accurancy = accuracy,
            arrival = arrival,
            communication = communication,
            location = location,
            priceQuality = priceQuality,
            convenience = convenience,
            feedback = feedback,
            date = date,
            relevance = relevance,
            accommodation = accommodationService.getById(accommodationId),
            booking = bookingService.getById(bookingId)
        )
        return accommodationRatingRepository.save(accommodationRating)
    }

    fun getById(id: Long): AccommodationRating {
        return accommodationRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation rating not found") }
    }

    fun update(
        id: Long,
        overallImpression: Int,
        putiry: Int,
        accurancy: Int,
        arrival: Int,
        communication: Int,
        location: Int,
        priceQuality: Int,
        convenience: Int,
        feedback: String,
        date: Date,
        relevance: Boolean
    ): AccommodationRating {
        val accommodationRating = accommodationRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation rating not found") }
        return accommodationRatingRepository.save(
            accommodationRating.copy(
                overallImpression = overallImpression,
                putiry = putiry,
                accurancy = accurancy,
                arrival = arrival,
                communication = communication,
                location = location,
                priceQuality = priceQuality,
                convenience = convenience,
                feedback = feedback,
                date = date,
                relevance = relevance
            )
        )
    }

    fun delete(id: Long) {
        accommodationRatingRepository.deleteById(id)
    }
}