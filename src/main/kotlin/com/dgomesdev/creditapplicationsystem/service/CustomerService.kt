package com.dgomesdev.creditapplicationsystem.service

import com.dgomesdev.creditapplicationsystem.model.Customer

interface CustomerService {

    fun saveCustomer(customer: Customer)

    fun updateCustomer(customerId: Long)

    fun findCustomerById(customerId: Long): Customer

    fun deleteCustomer(customerId: Long)
}