package com.jellyone.blps.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.jellyone.blps.domain.AccommodationRating

interface AccommodationRatingRepository : JpaRepository<AccommodationRating, Long> {
}