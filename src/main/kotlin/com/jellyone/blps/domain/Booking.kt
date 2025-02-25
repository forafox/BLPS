package com.jellyone.blps.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "bookings")
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val arrivalDate: Date,
    @Column
    val departureDate: Date,
    @Column
    val questCount: Int,
    @Column
    val price: Int,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val quest: User,
    @ManyToOne
    @JoinColumn(name = "accommodation_id")
    val accommodation: Accommodation,
)