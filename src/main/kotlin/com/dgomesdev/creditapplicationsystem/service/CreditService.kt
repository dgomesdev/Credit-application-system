package com.dgomesdev.creditapplicationsystem.service

import com.dgomesdev.creditapplicationsystem.model.Credit
import java.util.*

interface CreditService {

    fun saveCredit(credit: Credit)

    fun findAllCreditsByCustomerId(customerId: Long): List<Credit>

    fun findCreditByCreditCode(customerId: Long, creditCode: UUID): Credit
}