package com.pds.localpos.userservice.model

import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(
    name = "roles",
    indexes = [Index(name = "idx_roles_name", columnList = "name")]
)
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true, length = 50)
    var name: String,

    @Column(length = 255)
    var description: String? = null,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    var users: MutableSet<User> = mutableSetOf()
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Role
        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        val truncatedDesc = description?.let {
            if (it.length > 50) it.substring(0, 50) + "..." else it
        }
        return "Role(id=$id, name=$name, description=$truncatedDesc)"
    }
}
