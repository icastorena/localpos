package com.pds.localpos.userservice.model

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.time.Instant

@Entity
@Table(
    name = "stores",
    indexes = [Index(name = "idx_stores_name", columnList = "name")]
)
data class Store(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 4)
    var code: String,

    @Column(nullable = false, length = 150)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var address: String? = null,

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY)
    var users: MutableSet<User> = mutableSetOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @Column(name = "updated_at")
    var updatedAt: Instant? = null
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
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Store
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        val truncatedAddress = address?.let {
            if (it.length > 50) it.substring(0, 50) + "..." else it
        }
        return "Store(id=$id, name=$name, address=$truncatedAddress, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}
