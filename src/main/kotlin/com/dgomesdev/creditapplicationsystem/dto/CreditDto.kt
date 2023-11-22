package com.dgomesdev.creditapplicationsystem.dto

import jakarta.validation.constraints.Future
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    @field:NotNull(message = "Credit value is mandatory")
    @field:Min(value = 0, message = "Credit value cannot be negative")
    val creditValue: BigDecimal,
    @field:NotNull(message = "Credit value is mandatory")
    @field:Future(message = "Day of first installment cannot tbe in a past date")
    val dayOfFirstInstallment: LocalDate,
    @field:NotNull(message = "Number of installments is mandatory")
    @field:Min(value = 1, message = "Number of installments must be at least 1")
    val numberOfInstallments: Int,
    @field:NotNull(message = "Customer Id is mandatory")
    val customerId: Long
)