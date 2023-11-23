package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.exception.BusinessException
import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.mapper.toCredit
import com.dgomesdev.creditapplicationsystem.repository.CreditRepository
import com.dgomesdev.creditapplicationsystem.service.CreditService
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

@Service
class CreditServiceImpl(
    private val creditRepository: CreditRepository,
    private val customerService: CustomerServiceImpl
) : CreditService {
    override fun saveCredit(creditDto: CreditDto) {
        val customer = customerService.findCustomerById(creditDto.customerId)
        val credit = creditDto.toCredit().copy(customer = customer)
        if (credit.dayOfFirstInstallment.isAfter(LocalDate.now().plusMonths(3))) {
            throw BusinessException("Day of first installment cannot be later 3 months from today")
        } else creditRepository.save(credit)
    }

    override fun findAllCreditsByCustomerId(customerId: Long): List<Credit> {
        customerService.findCustomerById(customerId)
        return creditRepository.findAllByCustomerId(customerId)
    }

    override fun findCreditByCreditCode(customerId: Long, creditCode: UUID): Credit {
        customerService.findCustomerById(customerId)
        val credit: Credit = (
                creditRepository.findByCreditCode(creditCode)
                    ?: throw BusinessException("Credit code $creditCode not found")
                )
        return if (credit.customer?.id == customerId) credit else throw IllegalArgumentException("Unauthorized request!")
    }
}