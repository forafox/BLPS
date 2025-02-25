package com.jellyone.blps.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "guest_ratings")
data class QuestRating(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val rating: Int,
    @Column
    val feedback: String,
    @Column(name = "day")
    val date: Date,
    @Column
    var relevance: Boolean,
    @ManyToOne
    @JoinColumn(name = "quest_id")
    val quest: User,
    @ManyToOne
    @JoinColumn(name = "booking_id")
    val booking: Booking,
)