package com.jellyone.blps.repository

import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class RatingRepository(
    private val questRatingRepository: QuestRatingRepository,
    private val accommodationRatingRepository: AccommodationRatingRepository
) {
    @Transactional
    fun hasMultipleRatingsForBooking(bookingId: Long, isQuest: Boolean): Boolean {
        val questRatingCount = questRatingRepository.countByBookingId(bookingId)
        val accommodationRatingCount = accommodationRatingRepository.countByBookingId(bookingId)
        val multipleRatings = (questRatingCount > 0 && !isQuest) || (accommodationRatingCount > 0 && isQuest)

        if (multipleRatings) {
            val questRatings = questRatingRepository.findByBookingId(bookingId)
            val accommodationRatings = accommodationRatingRepository.findByBookingId(bookingId)

            questRatings.forEach { it.relevance = true }
            accommodationRatings.forEach { it.relevance = true }

            questRatingRepository.saveAll(questRatings)
            accommodationRatingRepository.saveAll(accommodationRatings)
        }

        return multipleRatings
    }
}
