package com.dgomesdev.creditapplicationsystem.repository

import com.dgomesdev.creditapplicationsystem.model.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CreditRepository: JpaRepository<Credit, Long> {

    @Query(value = "SELECT * FROM Credit WHERE CUSTOMER_ID = ?1", nativeQuery = true)
    fun findAllByCustomerId(customerId: Long): List<Credit>

    fun findByCreditCode(creditCode: UUID): Credit?
}