package com.jellyone.blps.domain

import jakarta.persistence.*

@Entity
@Table(name = "accommodation_private_raitings")
class AccommodationPrivateRating (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val privateText: String
)
