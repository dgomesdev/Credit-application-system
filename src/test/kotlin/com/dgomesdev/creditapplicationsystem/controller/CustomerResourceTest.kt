package com.dgomesdev.creditapplicationsystem.controller

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.model.mapper.toCustomer
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
import kotlin.random.Random

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ContextConfiguration
class CustomerResourceTest {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    companion object {
        const val URL = "/api/customers"
    }

    @BeforeEach
    fun setup() = customerRepository.deleteAll()

    @AfterEach
    fun tearDown() = customerRepository.deleteAll()

    @Test
    fun saveCustomer_return201Status() {
        //Given
        val customerDto = buildCustomerDto()
        val customerDtoAsString = objectMapper.writeValueAsString(customerDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun save2CustomersWithSameData_throwConflictException() {
        //Given
        customerRepository.save(buildCustomerDto().toCustomer())
        val customerDto = buildCustomerDto()
        val customerDtoAsString = objectMapper.writeValueAsString(customerDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isConflict)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Conflict of data! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun saveCustomerWithEmptyData_throwBadRequestException() {
        //Given
        val customerDto = buildCustomerDto(firstName = "")
        val customerDtoAsString = objectMapper.writeValueAsString(customerDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Bad request! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideCustomerId_findCustomerById_return200Status() {
        //Given
        val customer: Customer = customerRepository.save(buildCustomerDto().toCustomer())

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideInvalidId_return400Status() {
        //Given
        val invalidId: Long = Random.nextLong()
        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("$URL/$invalidId").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Error! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun findAllCustomers_return200() {
        //Given
        customerRepository.save(buildCustomerDto().toCustomer())

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get(URL).accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideCustomerId_deleteCustomer() {
        //Given
        val customer: Customer = customerRepository.save(buildCustomerDto().toCustomer())

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/${customer.id}").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideInvalidId_deleteCustomer_return400Status() {
        //Given
        val invalidId: Long = Random.nextLong()
        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.delete("$URL/$invalidId").accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Error! Consult the documentation"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.timeStamp").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.details[*]").isNotEmpty)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideCustomerId_updateCustomer_return200() {
        //Given
        val customer: Customer = customerRepository.save(buildCustomerDto().toCustomer())
        val customerUpdateDto: CustomerUpdateDto = buildCustomerUpdateDto()
        val customerUpdateDtoAsString = objectMapper.writeValueAsString(customerUpdateDto)

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=${customer.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerUpdateDtoAsString)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
    }

    @Test
    fun provideCustomerId_updateCustomer_return400() {
        //Given
        val invalidId: Long = Random.nextLong()

        //When
        //Then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("$URL?customerId=$invalidId").accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
    }

    private fun buildCustomerDto(firstName: String = "Dgomes") = CustomerDto(
        firstName = firstName,
        lastName = "Dev",
        cpf = "59113759078",
        income = BigDecimal.valueOf(1000),
        email = "dgomesdev@dgomesdev.com",
        password = "123",
        zipCode = "123",
        street = "Rua do Pepito"
    )

    private fun buildCustomerUpdateDto(firstName: String = "Danilo") = CustomerUpdateDto(
        firstName = firstName,
        lastName = "Gomes",
        income = BigDecimal.valueOf(500),
        zipCode = "123",
        street = "Rua do Pepito"
    )
}