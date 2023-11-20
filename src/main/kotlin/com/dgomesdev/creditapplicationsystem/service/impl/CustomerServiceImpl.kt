package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.repository.CustomerRepository
import com.dgomesdev.creditapplicationsystem.service.CustomerService
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
): CustomerService {
    override fun saveCustomer(customer: Customer) {
        customerRepository.save(customer)
    }

    override fun updateCustomer(customerId: Long) {
        TODO("Not yet implemented")
    }

    override fun findCustomerById(customerId: Long): Customer =
        customerRepository.findById(customerId).orElseThrow{
            throw RuntimeException("Id $customerId not found")
        }


    override fun deleteCustomer(customerId: Long) {
        customerRepository.deleteById(customerId)
    }
}