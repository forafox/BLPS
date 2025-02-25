package com.jellyone.blps.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.jellyone.blps.domain.AccommodationRating

interface AccommodationRatingRepository : JpaRepository<AccommodationRating, Long> {
    fun findAllByAccommodationIdAndRelevanceIsTrue(accommodationId: Long): List<AccommodationRating>
    fun countByBookingId(bookingId: Long): Long
    fun findByBookingId(bookingId: Long): List<AccommodationRating>
}