package com.dgomesdev.creditapplicationsystem.dto

import com.dgomesdev.creditapplicationsystem.model.Customer

data class CustomerListView(
    val id: Long,
    val firstName: String,
    val lastName: String
) {
    constructor(customer: Customer) : this(
        customer.id ?: 0,
        customer.firstName,
        customer.lastName
    )
}