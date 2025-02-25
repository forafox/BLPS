package com.jellyone.blps.repository

import com.jellyone.blps.domain.Accommodation
import org.springframework.data.jpa.repository.JpaRepository

interface AccommodationRepository : JpaRepository<Accommodation, Long> {
}