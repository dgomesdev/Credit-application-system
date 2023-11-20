package com.dgomesdev.creditapplicationsystem.dto

import java.math.BigDecimal
import java.time.LocalDate

data class CreditDto(
    val creditValue: BigDecimal,
    val dayOfFirstInstallment: LocalDate,
    val numberOfInstallments: Int,
    val customerId: Long
)