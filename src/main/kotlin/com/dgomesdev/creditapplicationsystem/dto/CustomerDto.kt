package com.dgomesdev.creditapplicationsystem.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.br.CPF
import java.math.BigDecimal

data class CustomerDto(
    @field:NotEmpty(message = "First name is mandatory")
    val firstName: String,
    @field:NotEmpty(message = "Last name is mandatory")
    val lastName: String,
    @field:NotEmpty(message = "CPF is mandatory")
    @field:CPF(message = "Invalid CPF")
    val cpf: String,
    @field:NotNull(message = "Income is mandatory")
    val income: BigDecimal,
    @field:NotEmpty(message = "E-mail is mandatory")
    @field:Email(message = "Invalid e-mail")
    val email: String,
    @field:NotEmpty(message = "Password is mandatory")
    val password: String,
    @field:NotEmpty(message = "Zip code is mandatory")
    val zipCode: String,
    @field:NotEmpty(message = "First name is mandatory")
    val street: String
)
