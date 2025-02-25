package com.jellyone.blps.domain

import com.jellyone.blps.domain.enums.Country
import jakarta.persistence.*

@Entity
@Table(name = "accommodations")
data class Accommodation(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val country: Country,
    @Column
    val city: String,
    @Column
    val address: String,
    @Column
    val price: Int,
    @Column
    val description: String,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "owner_id")
    val owner: User
)