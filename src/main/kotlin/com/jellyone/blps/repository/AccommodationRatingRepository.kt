package com.jellyone.blps.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.jellyone.blps.domain.AccommodationRating
import java.util.*

interface AccommodationRatingRepository : JpaRepository<AccommodationRating, Long> {
    fun findAllByAccommodationIdAndRelevanceIsTrue(accommodationId: Long): List<AccommodationRating>
    fun countByBookingId(bookingId: Long): Long
    fun findByBookingId(bookingId: Long): List<AccommodationRating>
    fun findByRelevanceFalseAndDateBefore(date: Date): List<AccommodationRating>
}