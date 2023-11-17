package com.dgomesdev.creditapplicationsystem.model

data class Customer(
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val email: String,
    val password: String,
    val address: Address,
    val credits: MutableList<Credit>,
    val id: Long? = null
)
