package com.pds.localpos.userservice.model

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.Instant
import java.util.*

@Entity
@Table(
    name = "stores",
    schema = "users",
    indexes = [Index(name = "idx_stores_name", columnList = "name")]
)
class Store(

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true, length = 4)
    var code: String = "",

    @Column(nullable = false, length = 150)
    var name: String = "",

    @Column(columnDefinition = "TEXT")
    var address: String? = null,

    @ManyToMany(mappedBy = "stores", fetch = FetchType.LAZY)
    var users: MutableSet<User> = mutableSetOf(),

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) {
            return false
        }
        other as Store
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        val addr = address?.let { if (it.length > 50) it.substring(0, 50) + "..." else it }
        return "Store(id=$id, code=$code, name=$name, address=$addr, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
