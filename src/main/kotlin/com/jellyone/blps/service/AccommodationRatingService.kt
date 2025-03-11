package com.jellyone.blps.service

import com.jellyone.blps.domain.AccommodationPrivateRating
import com.jellyone.blps.domain.AccommodationRating
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.AccommodationPrivateRatingRepository
import com.jellyone.blps.repository.AccommodationRatingRepository
import com.jellyone.blps.repository.RatingRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccommodationRatingService(
    private val accommodationRatingRepository: AccommodationRatingRepository,
    private val accommodationPrivateRatingRepository: AccommodationPrivateRatingRepository,
    private val accommodationService: AccommodationService,
    private val bookingService: BookingService,
    private val ratingRepository: RatingRepository,
    private val userService: UserService
) {

    @Transactional
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
        accommodationId: Long,
        bookingId: Long,
        privateText: String,
        username: String
    ): AccommodationRating {
        checkAbilityToRateAccommodation(accommodationId, username, bookingId)
        val accommodationRating = AccommodationRating(
            id = 0,
            overallImpression = overallImpression,
            putiry = putiry,
            accuracy = accuracy,
            arrival = arrival,
            communication = communication,
            location = location,
            priceQuality = priceQuality,
            conveniences = convenience,
            feedback = feedback,
            date = date,
            relevance = ratingRepository.hasMultipleRatingsForBooking(bookingId, false),
            accommodation = accommodationService.getById(accommodationId),
            booking = bookingService.getById(bookingId)
        )
         accommodationRatingRepository.save(accommodationRating)
//        if (true) {
//            throw RuntimeException("Ошибка! Транзакция должна быть откатана.");
//        }
         accommodationPrivateRatingRepository.save(AccommodationPrivateRating(0, privateText))
        return accommodationRating
    }

    fun getById(id: Long): AccommodationRating {
        return accommodationRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation rating not found") }
    }

    fun getAllByAccommodationId(accommodationId: Long): List<AccommodationRating> {
        return accommodationRatingRepository.findAllByAccommodationIdAndRelevanceIsTrue(accommodationId)
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
    ): AccommodationRating {
        val accommodationRating = accommodationRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Accommodation rating not found") }
        return accommodationRatingRepository.save(
            accommodationRating.copy(
                overallImpression = overallImpression,
                putiry = putiry,
                accuracy = accurancy,
                arrival = arrival,
                communication = communication,
                location = location,
                priceQuality = priceQuality,
                conveniences = convenience,
                feedback = feedback,
                date = date,
                relevance = false
            )
        )
    }

    fun delete(id: Long) {
        accommodationRatingRepository.deleteById(id)
    }

    private fun checkAbilityToRateAccommodation(accommodationId: Long, username: String, bookingId: Long) {
        checkAccess(accommodationId, username)
        checkRules(bookingId)
    }

    private fun checkAccess(accommodationId: Long, username: String) {
        val accommodation = accommodationService.getById(accommodationId)
        val evaluator = userService.getByUsername(username)
        if (accommodation.owner.username == evaluator.username) {
            throw AccessDeniedException("You can't rate this accommodation")
        }
    }

    private fun checkRules(bookingId: Long) {
        val booking = bookingService.getById(bookingId)
        if (booking.departureDate.after(Date())) {
            throw IllegalStateException("There was no eviction from the house")
        }
        if (!isWithinLast14Days(booking.departureDate)) {
            throw IllegalStateException("The house was rented for more than 14 days")
        }
    }

    private fun isWithinLast14Days(date: Date): Boolean {
        val currentTime = System.currentTimeMillis()
        val fourteenDaysMillis = 14L * 24 * 60 * 60 * 1000 // 14 дней в миллисекундах

        return date.time >= (currentTime - fourteenDaysMillis)
    }
}