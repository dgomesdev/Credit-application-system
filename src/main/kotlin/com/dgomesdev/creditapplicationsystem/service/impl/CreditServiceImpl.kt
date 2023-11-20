package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.repository.CreditRepository
import com.dgomesdev.creditapplicationsystem.service.CreditService
import com.dgomesdev.creditapplicationsystem.service.CustomerService
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreditServiceImpl(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerService
): CreditService {
    override fun saveCredit(credit: Credit) {
        val savedCredit = credit.copy(
            customer = customerService.findCustomerById(credit.customer?.id!!)
        )
        creditRepository.save(savedCredit)
    }

    override fun findAllCreditsByCustomerId(customerId: Long): List<Credit> =
        creditRepository.findAllByCustomerId(customerId)

    override fun findCreditByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = (
                creditRepository.findByCreditCode(creditCode) ?:
                throw RuntimeException("Credit code not found")
                )
        return if (credit.customer?.id == customerId) credit else throw RuntimeException("Not authorized!")
    }
}