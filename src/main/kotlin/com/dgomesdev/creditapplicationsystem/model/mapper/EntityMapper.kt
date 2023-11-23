package com.dgomesdev.creditapplicationsystem.model.mapper

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.model.Address
import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.Customer

fun CustomerDto.toCustomer(): Customer =
    Customer(
        this.firstName,
        this.lastName,
        this.cpf,
        this.income,
        this.email,
        this.password,
        Address(this.zipCode, this.street)
    )

fun CustomerUpdateDto.toCustomer(customer: Customer): Customer =
    customer.copy(
        firstName = this.firstName,
        lastName = this.lastName,
        income = this.income,
        address = Address(this.zipCode, this.street)
    )

fun CreditDto.toCredit(): Credit =
    Credit(
        creditValue = this.creditValue,
        dayOfFirstInstallment = this.dayOfFirstInstallment,
        numberOfInstallments = this.numberOfInstallments
    )