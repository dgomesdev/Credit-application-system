package com.dgomesdev.creditapplicationsystem.repository

import com.dgomesdev.creditapplicationsystem.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer, Long>