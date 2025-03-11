package com.jellyone.blps.repository

import com.jellyone.blps.domain.GuestRating
import org.springframework.data.jpa.repository.JpaRepository

interface GuestRatingRepository : JpaRepository<GuestRating, Long> {
    fun findAllByGuestIdAndRelevanceIsTrue(guestId: Long): List<GuestRating>
    fun countByBookingId(bookingId: Long): Long
    fun findByBookingId(bookingId: Long): List<GuestRating>
}