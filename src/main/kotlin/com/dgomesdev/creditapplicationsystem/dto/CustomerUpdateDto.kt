package com.dgomesdev.creditapplicationsystem.dto

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CustomerUpdateDto(
    @field:NotEmpty(message = "First name cannot be empty")
    val firstName: String,
    @field:NotEmpty(message = "Last name cannot be empty")
    val lastName: String,
    @field:NotNull(message = "Income cannot be empty")
    val income: BigDecimal,
    @field:NotEmpty(message = "Zip code cannot be empty")
    val zipCode: String,
    @field:NotEmpty(message = "Street cannot be empty")
    val street: String
)
