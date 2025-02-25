package com.jellyone.blps.domain

import com.jellyone.blps.domain.enums.Role
import jakarta.persistence.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @Column
    val username: String,
    @Column
    val password: String,
    @Column
    val name: String,
    @Column
    val surname: String,
    @Column
    val email: String,
    @Column
    val phone: String,
    @Column
    val role: Role
) : UserDetails {
    override fun getAuthorities(): Collection<SimpleGrantedAuthority> {
        val list = mutableListOf<SimpleGrantedAuthority>()
        list.add(SimpleGrantedAuthority(role.toString()))
        return list
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}