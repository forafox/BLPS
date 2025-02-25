package com.jellyone.blps.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "accommodation_raitings")
data class AccommodationRating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val overallImpression: Int,
    @Column
    val putiry: Int,
    @Column
    val accuracy: Int,
    @Column
    val arrival: Int,
    @Column
    val communication: Int,
    @Column
    val location: Int,
    @Column
    val priceQuality: Int,
    @Column
    val conveniences: Int,
    @Column
    val feedback: String,
    @Column(name = "day")
    val date: Date,
    @Column
    var relevance: Boolean,
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    val accommodation: Accommodation,
    @ManyToOne
    @JoinColumn(name = "booking_id")
    val booking: Booking
)