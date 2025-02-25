package com.jellyone.blps.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "accommodation_raitings")
data class AccommodationRaiting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val overallImpression: Int,
    @Column
    val putiry: Int,
    @Column
    val accurancy: Int,
    @Column
    val arrival: Int,
    @Column
    val communication: Int,
    @Column
    val location: Int,
    @Column
    val priceQuality: Int,
    @Column
    val convenience: Int,
    @Column
    val feedback: String,
    @Column
    val date: Date,
    @Column
    val relevance: Boolean,
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    val accommodation: Accommodation,
    @ManyToOne
    @JoinColumn(name = "booking_id")
    val booking: Booking
)