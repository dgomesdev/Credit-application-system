package com.dgomesdev.creditapplicationsystem.service.impl

import com.dgomesdev.creditapplicationsystem.dto.CustomerDto
import com.dgomesdev.creditapplicationsystem.dto.CustomerUpdateDto
import com.dgomesdev.creditapplicationsystem.exception.BusinessException
import com.dgomesdev.creditapplicationsystem.model.Customer
import com.dgomesdev.creditapplicationsystem.model.mapper.toCustomer
import com.dgomesdev.creditapplicationsystem.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ActiveProfiles
import java.math.BigDecimal
import java.util.*
import kotlin.random.Random

@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerServiceImplTest {

    @MockK
    lateinit var customerRepository: CustomerRepository
    @InjectMockKs
    lateinit var customerService: CustomerServiceImpl

    @Test
    fun addNewCustomer_saveCustomer() {
        //Given
        val fakeCustomerDto: CustomerDto = buildCustomer()
        val fakeCustomer: Customer = fakeCustomerDto.toCustomer()
        every { customerRepository.save(any()) } returns fakeCustomer

        //When
        val actual = customerService.saveCustomer(fakeCustomerDto)

        //Then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(Unit)
        verify(exactly = 1) { customerRepository.save(fakeCustomer) }
    }

    @Test
    fun informId_updateCustomer() {
        //Given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().toCustomer().copy(id = fakeId)
        val fakeCustomerUpdateDto = updateCustomer()
        val fakeUpdatedCustomer = fakeCustomerUpdateDto.toCustomer(fakeCustomer)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.save(fakeUpdatedCustomer) } returns fakeUpdatedCustomer

        //When
        val actual = customerService.updateCustomer(fakeId, fakeCustomerUpdateDto)

        //Then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isSameAs(Unit)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
        verify(exactly = 1) { customerRepository.save(fakeUpdatedCustomer) }
    }

    @Test
    fun informId_findCustomerById_returnsCustomer() {
        //Given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().toCustomer().copy(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)

        //When
        val actual: Customer = customerService.findCustomerById(fakeId)

        //Then
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isExactlyInstanceOf(Customer::class.java)
        Assertions.assertThat(actual).isSameAs(fakeCustomer)
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun `informId_doesn'tFindId_throwsException`() {
        //Given
        val fakeId: Long = Random.nextLong()
        every { customerRepository.findById(fakeId) } returns Optional.empty()

        //When
        //Then
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
            .isThrownBy { customerService.findCustomerById(fakeId) }
            .withMessage("Customer with id $fakeId not found")
        verify(exactly = 1) { customerRepository.findById(fakeId) }
    }

    @Test
    fun informId_deleteCustomer() {
        //Given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().toCustomer().copy(id = fakeId)
        every { customerRepository.findById(fakeId) } returns Optional.of(fakeCustomer)
        every { customerRepository.delete(fakeCustomer) } just runs
        //When
        customerService.deleteCustomer(fakeId)

        //Then
        verify(exactly = 1) { customerRepository.findById(fakeId) }
        verify(exactly = 1) { customerRepository.delete(fakeCustomer) }
    }

    @Test
    fun findAllCustomers_returnsListOfCustomers() {
        //Given
        val fakeId: Long = Random.nextLong()
        val fakeCustomer: Customer = buildCustomer().toCustomer().copy(id = fakeId)
        val fakeListOfCustomers: List<Customer> = listOf(fakeCustomer)
        every { customerRepository.findAll() } returns fakeListOfCustomers

        //When
        val actual: List<Customer> = customerService.findAllCustomers()

        //Then
        Assertions.assertThat(actual).isSameAs(fakeListOfCustomers)
        verify(exactly = 1) { customerRepository.findAll() }
    }

    private fun buildCustomer() = CustomerDto(
        firstName = "Pepito",
        lastName = "Gomes",
        cpf = "01297273265",
        income = BigDecimal.valueOf(1000),
        email = "pepeito@pepito.com",
        password = "123",
        zipCode = "123",
        street = "Rua do Pepito"
    )

    private fun updateCustomer() = CustomerUpdateDto(
        firstName = "Ursinho",
        lastName = "Dev",
        income = BigDecimal.valueOf(500),
        zipCode = "123",
        street = "Rua do Pepito"
    )
}