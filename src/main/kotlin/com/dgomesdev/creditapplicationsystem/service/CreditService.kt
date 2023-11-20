package com.dgomesdev.creditapplicationsystem.service

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.model.Credit
import java.util.*

interface CreditService {

    fun saveCredit(creditDto: CreditDto)

    fun findAllCreditsByCustomerId(customerId: Long): List<Credit>

    fun findCreditByCreditCode(customerId: Long, creditCode: UUID): Credit

    fun CreditDto.toEntity(): Credit
}