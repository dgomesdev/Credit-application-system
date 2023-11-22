package com.dgomesdev.creditapplicationsystem.model

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
data class Customer(
    @Column(nullable = false) val firstName: String,
    @Column(nullable = false) val lastName: String,
    @Column(nullable = false, unique = true) val cpf: String,
    @Column(nullable = false) val income: BigDecimal,
    @Column(nullable = false, unique = true) val email: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) @Embedded val address: Address,
    @Column(nullable = false) @OneToMany(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE, CascadeType.PERSIST],
        mappedBy = "customer"
    ) val credits: MutableList<Credit> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)
