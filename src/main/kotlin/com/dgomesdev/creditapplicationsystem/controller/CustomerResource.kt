package com.dgomesdev.creditapplicationsystem.controller

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerListView
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerView
import com.dgomesdev.creditapplicationsystem.service.CustomerService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/customers")
class CustomerResource(
    private val customerService: CustomerService
) {

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<String> {
        return try {
            customerService.saveCustomer(customerDto)
            ResponseEntity.status(HttpStatus.CREATED).body("Customer saved!")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error when saving customer: ${e.message}")
        }
    }

    @GetMapping("/{id}")
    fun findCustomerById(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            val customer = CustomerView(customerService.findCustomerById(id))
            ResponseEntity.status(HttpStatus.OK).body(
                "Name: ${customer.firstName} ${customer.lastName} " +
                "CPF: ${customer.cpf} " +
                "Income: ${customer.income} " +
                "E-mail: ${customer.email} " +
                "Zip code: ${customer.zipCode} " +
                "Street: ${customer.street}"
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found!")
        }
    }

    @GetMapping
    fun findAllCustomers(): ResponseEntity<List<CustomerListView>> =
        try {
            val customerList = customerService
                .findAllCustomers()
                .stream()
                .map { customer -> CustomerListView(customer) }
                .collect(Collectors.toList())
            ResponseEntity.status(HttpStatus.OK).body(customerList)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emptyList())
        }

    @DeleteMapping("/{id}")
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<String> {
        return try {
            customerService.deleteCustomer(id)
            ResponseEntity.status(HttpStatus.OK).body("Customer deleted successfully")
        } catch (e:Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found!")
        }
    }

    @PatchMapping
    fun updateCustomer(@RequestParam(value = "customerId") id: Long,
                       @RequestBody @Valid customerUpdateDto: CustomerUpdateDto): ResponseEntity<String> {
        return try {
            customerService.updateCustomer(id, customerUpdateDto)
            ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully!")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error when updating customer: ${e.message}")
        }
    }
}