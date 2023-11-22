package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.exception.BusinessException
import com.dgomesdev.creditapplicationsystem.model.Address
import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.repository.CustomerRepository
import com.dgomesdev.creditapplicationsystem.service.CustomerService
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository
): CustomerService {
    override fun saveCustomer(customerDto: CustomerDto) {
        customerRepository.save(customerDto.toEntity())
    }

    override fun updateCustomer(customerId: Long, customerUpdateDto: CustomerUpdateDto) {
        val customer = customerRepository.findById(customerId).orElseThrow {
            throw BusinessException("Customer with id $customerId not found")
        }
        val updatedCustomer = customerUpdateDto.toEntity(customer)
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

    override fun CustomerDto.toEntity(): Customer =
        Customer(
            this.firstName,
            this.lastName,
            this.cpf,
            this.income,
            this.email,
            this.password,
            Address(this.zipCode, this.street),
            credits = mutableListOf()
        )

    override fun CustomerUpdateDto.toEntity(customer: Customer): Customer =
        customer.copy(
            firstName = this.firstName,
            lastName = this.lastName,
            income = this.income,
            address = Address(this.zipCode, this.street)
        )
}