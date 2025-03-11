package com.jellyone.blps.service

import com.jellyone.blps.domain.GuestRating
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.GuestRatingRepository
import com.jellyone.blps.repository.RatingRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.*

@Service
class GuestRatingService(
    private val guestRatingRepository: GuestRatingRepository,
    private val userService: UserService,
    private val bookingService: BookingService,
    private val ratingRepository: RatingRepository
) {
    fun create(
        rating: Int,
        feedback: String,
        date: Date,
        guestId: Long,
        bookingId: Long,
        username: String
    ): GuestRating {
        checkAbilityToRateAccommodation(guestId, username)
        val guestRating = GuestRating(
            id = 0,
            rating = rating,
            feedback = feedback,
            date = date,
            guest = userService.getById(guestId),
            relevance = ratingRepository.hasMultipleRatingsForBooking(bookingId, true),
            booking = bookingService.getById(bookingId)
        )
        return guestRatingRepository.save(guestRating)
    }

    fun getById(id: Long): GuestRating {
        return guestRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Guest rating not found") }
    }

    fun getAllByGuestId(guestId: Long): List<GuestRating> {
        return guestRatingRepository.findAllByGuestIdAndRelevanceIsTrue(guestId)
    }

    fun update(
        id: Long,
        rating: Int,
        feedback: String,
        date: Date,
    ): GuestRating {
        val guestRating = guestRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Guest rating not found") }
        return guestRatingRepository.save(
            guestRating.copy(
                rating = rating,
                feedback = feedback,
                date = date,
                relevance = false
            )
        )
    }

    fun delete(id: Long) {
        guestRatingRepository.deleteById(id)
    }

    fun checkAbilityToRateAccommodation(guestId: Long, username: String) {
        val guest = userService.getById(guestId)
        val evaluator = userService.getByUsername(username)
        if (guest.username == evaluator.username) {
            throw AccessDeniedException("You can't rate")
        }
    }
}