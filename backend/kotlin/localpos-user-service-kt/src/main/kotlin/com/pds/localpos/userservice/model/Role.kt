package com.pds.localpos.userservice.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "roles",
    schema = "users",
    indexes = [Index(name = "idx_roles_name", columnList = "name")]
)
class Role(

    @Id
    @Column(length = 36, updatable = false, nullable = false)
    var id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true, length = 50)
    var name: String = "",

    @Column(length = 255)
    var description: String? = null,

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    var users: MutableSet<User> = mutableSetOf()
)
