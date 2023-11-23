package com.dgomesdev.creditapplicationsystem.repository

import com.dgomesdev.creditapplicationsystem.model.Address
import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.time.LocalDate

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {

    @Autowired lateinit var creditRepository: CreditRepository
    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1: Credit
    private lateinit var credit2: Credit

    @BeforeEach fun setup() {
        customer = testEntityManager.persist(buildCustomer())
        credit1 = testEntityManager.persist(buildCredit().copy(customer = customer))
        credit2 = testEntityManager.persist(buildCredit().copy(customer = customer))
    }

    @Test
    fun addCredits_findCreditByCreditCode_returnCredit() {
        //Given
        val fakeCreditCode1 = credit1.creditCode
        val fakeCreditCode2 = credit2.creditCode

        //When
        val fakeCredit1: Credit? = creditRepository.findByCreditCode(fakeCreditCode1)
        val fakeCredit2: Credit? = creditRepository.findByCreditCode(fakeCreditCode2)

        //Then
        Assertions.assertThat(fakeCredit1).isNotNull
        Assertions.assertThat(fakeCredit2).isNotNull
        Assertions.assertThat(fakeCredit1).isSameAs(credit1)
        Assertions.assertThat(fakeCredit2).isSameAs(credit2)
    }

    @Test
    fun informCustomerId_findCreditsBYCustomerId_returnListOfCredits() {
        //Given
        val fakeCustomerId = customer.id!!

        //When
        val fakeListOfCredits: List<Credit> = creditRepository.findAllByCustomerId(fakeCustomerId)

        //Then
        Assertions.assertThat(fakeListOfCredits).isNotEmpty
        Assertions.assertThat(fakeListOfCredits.size).isEqualTo(2)
        Assertions.assertThat(fakeListOfCredits).contains(credit1, credit2)
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

    private fun buildCredit() = Credit(
        creditValue = BigDecimal.ONE,
        dayOfFirstInstallment = LocalDate.now().plusDays(1),
        numberOfInstallments = 10
    )
}