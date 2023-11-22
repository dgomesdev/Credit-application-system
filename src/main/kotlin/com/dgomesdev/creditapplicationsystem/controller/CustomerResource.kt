package com.dgomesdev.creditapplicationsystem.controller

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerListView
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerView
import com.dgomesdev.creditapplicationsystem.service.CustomerService
import com.dgomesdev.creditapplicationsystem.service.impl.CustomerServiceImpl
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
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/customers")
class CustomerResource(
    private val customerService: CustomerServiceImpl
) {

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customerDto: CustomerDto): ResponseEntity<String> {
        customerService.saveCustomer(customerDto)
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer ${customerDto.firstName} saved!")
    }

    @GetMapping("/{id}")
    fun findCustomerById(@PathVariable id: Long): ResponseEntity<String> {
        val customer = CustomerView(customerService.findCustomerById(id))
        return ResponseEntity.status(HttpStatus.OK).body(
            "Name: ${customer.firstName} ${customer.lastName} \n" +
                    "CPF: ${customer.cpf} \n" +
                    "Income: ${customer.income} \n" +
                    "E-mail: ${customer.email} \n" +
                    "Zip code: ${customer.zipCode} \n" +
                    "Street: ${customer.street} \n"
        )
    }

    @GetMapping
    fun findAllCustomers(): ResponseEntity<List<CustomerListView>> {
        val customerList = customerService
            .findAllCustomers()
            .stream()
            .map { customer -> CustomerListView(customer) }
            .collect(Collectors.toList())
        return ResponseEntity.status(HttpStatus.OK).body(customerList)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Long): ResponseEntity<String> {
        customerService.deleteCustomer(id)
        return ResponseEntity.status(HttpStatus.OK).body("Customer deleted successfully")
    }

    @PatchMapping
    fun updateCustomer(
        @RequestParam(value = "customerId") id: Long,
        @RequestBody @Valid customerUpdateDto: CustomerUpdateDto
    ): ResponseEntity<String> {
        customerService.updateCustomer(id, customerUpdateDto)
        return ResponseEntity.status(HttpStatus.OK).body("Customer updated successfully!")
    }
}