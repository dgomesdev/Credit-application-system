package com.dgomesdev.creditapplicationsystem.repository

import com.dgomesdev.creditapplicationsystem.model.Credit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository: JpaRepository<Credit, Long>