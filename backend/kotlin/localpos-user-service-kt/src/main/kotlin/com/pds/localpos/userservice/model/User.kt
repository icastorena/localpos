package com.pds.localpos.userservice.model

import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "users",
    schema = "users",
    indexes = [
        Index(name = "idx_users_username", columnList = "username"),
        Index(name = "idx_users_email", columnList = "email")
    ]
)
class User(

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true, length = 100)
    var username: String = "",

    @Column(nullable = false, length = 255)
    var password: String = "",

    @Column(unique = true, length = 150)
    var email: String? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_stores",
        schema = "users",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "store_id")]
    )
    var stores: MutableSet<Store> = mutableSetOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        schema = "users",
        joinColumns = [JoinColumn(name = "user_id", nullable = false)],
        inverseJoinColumns = [JoinColumn(name = "role_id", nullable = false)]
    )
    var roles: MutableSet<Role> = mutableSetOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
) {
    @PrePersist
    fun onCreate() {
        val now = Instant.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = Instant.now()
    }
}
