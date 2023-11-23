package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.exception.BusinessException
import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.model.mapper.toCustomer
import com.dgomesdev.creditapplicationsystem.repository.CustomerRepository
import com.dgomesdev.creditapplicationsystem.service.CustomerService
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
): CustomerService {
    override fun saveCustomer(customerDto: CustomerDto) {
        customerRepository.save(customerDto.toCustomer())
    }

    override fun updateCustomer(customerId: Long, customerUpdateDto: CustomerUpdateDto) {
        val customer = this.findCustomerById(customerId)
        val updatedCustomer = customerUpdateDto.toCustomer(customer)
        customerRepository.save(updatedCustomer)
    }

    override fun findCustomerById(customerId: Long): Customer =
        customerRepository.findById(customerId).orElseThrow{
            throw BusinessException("Customer with id $customerId not found")
        }


    override fun deleteCustomer(customerId: Long) {
        val customer = this.findCustomerById(customerId)
        customerRepository.delete(customer)
    }

    override fun findAllCustomers(): List<Customer> = customerRepository.findAll()
}