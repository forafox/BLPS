package com.jellyone.blps.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "private_feedback")
data class PrivateFeedback (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val feedback: String,
    @Column(name = "day")
    val date: Date,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "owner_id")
    val owner: User,
    @ManyToOne
    @JoinColumn(name = "booking_id")
    val booking: Booking
)