package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.exception.BusinessException
import com.dgomesdev.creditapplicationsystem.model.Address
import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.model.mapper.toCredit
import com.dgomesdev.creditapplicationsystem.repository.CreditRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CreditServiceImplTest {

    @MockK
    lateinit var creditRepository: CreditRepository

    @MockK
    lateinit var customerService: CustomerServiceImpl

    @InjectMockKs
    lateinit var creditService: CreditServiceImpl

    @Test
    fun addNewCustomer_saveCredit() {
        //Given
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().copy(id = fakeCustomerId)
        val fakeCreditDto: CreditDto = buildCredit(fakeCustomerId)
        val fakeCredit: Credit = fakeCreditDto.toCredit()
        every { customerService.findCustomerById(fakeCreditDto.customerId) } returns fakeCustomer
        every { creditRepository.save(any()) } returns fakeCredit

        //When
        val actual = creditService.saveCredit(fakeCreditDto)

        //Then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isEqualTo(Unit)
        verify(exactly = 1) { customerService.findCustomerById(fakeCreditDto.customerId) }
        verify(exactly = 1) { creditRepository.save(any()) }

    }

    @Test
    fun saveCredit_throwsException() {
        //Given
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().copy(id = fakeCustomerId)
        val fakeCreditDto: CreditDto =
            buildCredit(fakeCustomerId, dayOfFirstInstallment = LocalDate.now().plusMonths(4))
        every { customerService.findCustomerById(fakeCreditDto.customerId) } returns fakeCustomer

        //When
        //Then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.saveCredit(fakeCreditDto) }
            .withMessage("Day of first installment cannot be later 3 months from today")
        verify(exactly = 0) { creditRepository.save(any()) }
    }


    @Test
    fun findAllCredits_returnsListOfCredits() {
        //Given
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().copy(id = fakeCustomerId)
        val fakeCredit: Credit = buildCredit(fakeCustomerId).toCredit()
        val fakeListOfCredits: List<Credit> = listOf(fakeCredit)
        every { customerService.findCustomerById(fakeCustomerId) } returns fakeCustomer
        every { creditRepository.findAllByCustomerId(fakeCustomerId) } returns fakeListOfCredits

        //When
        val actual: List<Credit> = creditService.findAllCreditsByCustomerId(fakeCustomerId)

        //Then
        Assertions.assertThat(actual).isSameAs(fakeListOfCredits)
        verify(exactly = 1) { creditRepository.findAllByCustomerId(fakeCustomerId) }
    }

    @Test
    fun informCustomerIdAndCreditCode_returnCredit() {
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().copy(id = fakeCustomerId)

        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(fakeCustomerId).toCredit().copy(creditCode = fakeCreditCode, customer = fakeCustomer)
        every { customerService.findCustomerById(fakeCustomerId) } returns fakeCustomer
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //When
        val actual = creditService.findCreditByCreditCode(fakeCustomerId, fakeCreditCode)

        //THen
        Assertions.assertThat(actual).isSameAs(fakeCredit)
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun informCustomerIdAndCreditCode_throwException() {
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().copy(id = fakeCustomerId)
        val fakeCreditCode: UUID = UUID.randomUUID()
        every { customerService.findCustomerById(fakeCustomerId) } returns fakeCustomer
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns null

        //When
        //THen
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { creditService.findCreditByCreditCode(fakeCustomerId, fakeCreditCode) }
            .withMessage("Credit code $fakeCreditCode not found")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    @Test
    fun `informCustomerIdAndCreditCode_idDoesn'tMatch_throwException`() {
        val fakeCustomerId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().copy(id = fakeCustomerId)

        val fakeCreditCode: UUID = UUID.randomUUID()
        val fakeCredit: Credit = buildCredit(Random.nextLong()).toCredit().copy(creditCode = fakeCreditCode)
        every { customerService.findCustomerById(fakeCustomerId) } returns fakeCustomer
        every { creditRepository.findByCreditCode(fakeCreditCode) } returns fakeCredit

        //When
        //THen
        Assertions.assertThatExceptionOfType(IllegalArgumentException::class.java)
            .isThrownBy { creditService.findCreditByCreditCode(fakeCustomerId, fakeCreditCode) }
            .withMessage("Unauthorized request!")
        verify(exactly = 1) { creditRepository.findByCreditCode(fakeCreditCode) }
    }

    private fun buildCustomer() = Customer(
        firstName = "Pepito",
        lastName = "Gomes",
        cpf = "01297273265",
        income = BigDecimal.valueOf(1000),
        email = "pepeito@pepito.com",
        password = "123",
        Address(
            zipCode = "123",
            street = "Rua do Pepito"
        )
    )

    private fun buildCredit(customerId: Long, dayOfFirstInstallment: LocalDate = LocalDate.now().plusDays(1)) =
        CreditDto(
            creditValue = BigDecimal.ONE,
            dayOfFirstInstallment = dayOfFirstInstallment,
            numberOfInstallments = 10,
            customerId = customerId
        )

}