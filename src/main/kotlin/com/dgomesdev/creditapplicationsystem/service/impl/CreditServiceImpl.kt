package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.exception.BusinessException
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
    override fun saveCredit(creditDto: CreditDto) {
        val customer = customerService.findCustomerById(creditDto.customerId)
        val credit = creditDto.toEntity().copy(customer = customer)
        creditRepository.save(credit)
    }

    override fun findAllCreditsByCustomerId(customerId: Long): List<Credit> =
        creditRepository.findAllByCustomerId(customerId)

    override fun findCreditByCreditCode(customerId: Long, creditCode: UUID): Credit {
        val credit: Credit = (
                creditRepository.findByCreditCode(creditCode) ?:
                throw BusinessException("Credit code not found")
                )
        return if (credit.customer?.id == customerId) credit else throw IllegalArgumentException("Not authorized!")
    }

    override fun CreditDto.toEntity(): Credit =
        Credit(
            creditValue = this.creditValue,
            dayOfFirstInstallment = this.dayOfFirstInstallment,
            numberOfInstallments = this.numberOfInstallments
        )
}