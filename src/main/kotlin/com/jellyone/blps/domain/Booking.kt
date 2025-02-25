package com.jellyone.blps.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "bookings")
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column(name = "arrival_day")
    val arrivalDate: Date,
    @Column(name = "departure_day")
    val departureDate: Date,
    @Column(name = "guest_count")
    val questCount: Int,
    @Column
    val price: Int,
    @ManyToOne
    @JoinColumn(name = "quest_id")
    val quest: User,
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    val accommodation: Accommodation,
)