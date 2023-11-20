package com.dgomesdev.creditapplicationsystem.dto

import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.enummeration.Status
import java.math.BigDecimal
import java.util.UUID

data class CreditListView(
    val creditCode: UUID,
    val creditValue: BigDecimal,
    val status: Status
) {
    constructor(credit: Credit) : this(
        credit.creditCode,
        credit.creditValue,
        credit.status
    )
}