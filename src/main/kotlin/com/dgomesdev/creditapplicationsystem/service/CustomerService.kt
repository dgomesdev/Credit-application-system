package com.dgomesdev.creditapplicationsystem.service

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.model.Customer

interface CustomerService {

    fun saveCustomer(customerDto: CustomerDto)

    fun updateCustomer(customerId: Long, customerUpdateDto: CustomerUpdateDto)

    fun findCustomerById(customerId: Long): Customer

    fun deleteCustomer(customerId: Long)

    fun findAllCustomers(): List<Customer>

    fun CustomerDto.toEntity(): Customer

    fun CustomerUpdateDto.toEntity(customer: Customer): Customer
}