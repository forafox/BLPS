package com.jellyone.blps.repository

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class RatingRepository(
    private val guestRatingRepository: GuestRatingRepository,
    private val accommodationRatingRepository: AccommodationRatingRepository
) {
    @Transactional
    fun hasMultipleRatingsForBooking(bookingId: Long, isGuest: Boolean): Boolean {
        val guestRatingCount = guestRatingRepository.countByBookingId(bookingId)
        val accommodationRatingCount = accommodationRatingRepository.countByBookingId(bookingId)
        val multipleRatings = (guestRatingCount > 0 && !isGuest) || (accommodationRatingCount > 0 && isGuest)

        if (multipleRatings) {
            val guestRatings = guestRatingRepository.findByBookingId(bookingId)
            val accommodationRatings = accommodationRatingRepository.findByBookingId(bookingId)

            guestRatings.forEach { it.relevance = true }
            accommodationRatings.forEach { it.relevance = true }

            guestRatingRepository.saveAll(guestRatings)
            accommodationRatingRepository.saveAll(accommodationRatings)
        }

        return multipleRatings
    }
}
