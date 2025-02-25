package com.jellyone.blps.repository

import com.jellyone.blps.domain.Booking
import org.springframework.data.jpa.repository.JpaRepository

interface BookingRepository : JpaRepository<Booking, Long> {
}