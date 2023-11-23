package com.dgomesdev.creditapplicationsystem.controller

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.model.Address
import com.dgomesdev.creditapplicationsystem.model.Credit
import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.repository.CreditRepository
import com.dgomesdev.creditapplicationsystem.repository.CustomerRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CreditResourceTest {

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL = "/api/credits"
    }

    @BeforeEach
    fun setup() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        creditRepository.deleteAll()
        customerRepository.deleteAll()
    }

    @Test
    fun saveCredit_return200() {
        //Given
        val fakeCustomer = customerRepository.save(buildCustomer())
        val creditDto = buildCreditDto(
            LocalDate.now(),
            numberOfInstallments = 48,
            customerId = fakeCustomer.id!!
        )
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideInvalidCustomerId_saveCredit_return400() {
        //Given
        val invalidCustomerId = Random.nextLong()
        val creditDto = buildCreditDto(
            LocalDate.now(),
            numberOfInstallments = 48,
            customerId = invalidCustomerId
        )
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }
    @Test
    fun provideInvalidDateOfFirstInstallment_saveCredit_return400() {
        //Given
        val fakeCustomer = customerRepository.save(buildCustomer())
        val creditDto = buildCreditDto(
            LocalDate.now().plusMonths(4),
            numberOfInstallments = 48,
            customerId = fakeCustomer.id!!
        )
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }
    @Test
    fun provideInvalidNumberOfInstallments_saveCredit_return400() {
        //Given
        val fakeCustomer = customerRepository.save(buildCustomer())
        val creditDto = buildCreditDto(
            LocalDate.now(),
            numberOfInstallments = 49,
            customerId = fakeCustomer.id!!
        )
        val creditDtoAsString = objectMapper.writeValueAsString(creditDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(creditDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun findAllCreditsByCustomerId_return200() {
        //Given
        val fakeCustomer = customerRepository.save(buildCustomer())

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=${fakeCustomer.id}").accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun findAllCreditsByCustomerId_return400() {
        //Given
        val invalidCustomerId = Random.nextLong()

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL?customerId=$invalidCustomerId").accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun findCreditByCreditCode_return200() {
        //Given
        val fakeCustomer = customerRepository.save(buildCustomer())
        val fakeCredit = creditRepository.save(
            buildCredit(
                customer = fakeCustomer
            )
        )

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/${fakeCredit.creditCode}?customerId=${fakeCustomer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun findCreditByCreditCode_return400() {
        //Given
        val fakeCustomer = customerRepository.save(buildCustomer())
        val fakeCreditCode = UUID.randomUUID()

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.get("$URL/$fakeCreditCode?customerId=${fakeCustomer.id}")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCreditDto(
        dayOfFirstInstallment: LocalDate,
        numberOfInstallments: Int,
        customerId: Long
    ) = CreditDto(
        creditValue = BigDecimal.ONE,
        dayOfFirstInstallment = dayOfFirstInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId
    )

    private fun buildCredit(
        customer: Customer
    ) = Credit(
        creditValue = BigDecimal.ONE,
        dayOfFirstInstallment = LocalDate.now(),
        numberOfInstallments = 48,
        customer = customer
    )

    private fun buildCustomer() = Customer(
        firstName = "Dgomes",
        lastName = "Dev",
        cpf = "59113759078",
        income = BigDecimal.valueOf(1000),
        email = "dgomesdev@dgomesdev.com",
        password = "123",
        Address(
            zipCode = "123",
            street = "Rua do Pepito"
        )
    )
}