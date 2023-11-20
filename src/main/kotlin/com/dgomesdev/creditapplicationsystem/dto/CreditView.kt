package com.dgomesdev.creditapplicationsystem.dto

import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.enummeration.Status
import java.math.BigDecimal
import java.util.*

data class CreditView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val numberOfInstallments: Int,
    val status: Status,
    val firstNameCustomer: String,
    val lastNameCustomer: String
) {
    constructor(credit: Credit) : this(
        credit.creditCode,
        credit.creditValue,
        credit.numberOfInstallments,
        credit.status,
        credit.customer?.firstName ?: "",
        credit.customer?.lastName ?: ""
    )
}
