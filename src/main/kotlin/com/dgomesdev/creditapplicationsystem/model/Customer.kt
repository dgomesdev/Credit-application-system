package com.dgomesdev.creditapplicationsystem.model

import jakarta.persistence.*

@Entity
data class Customer(
    @Column(nullable = false) val firstName: String,
    @Column(nullable = false) val lastName: String,
    @Column(nullable = false, unique = true) val cpf: String,
    @Column(nullable = false, unique = true) val email: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) @Embedded val address: Address,
    @Column(nullable = false) @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE, CascadeType.PERSIST],
        mappedBy = "customer"
    ) val credits: MutableList<Credit>,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)
