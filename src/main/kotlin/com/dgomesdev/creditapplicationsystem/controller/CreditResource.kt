package com.dgomesdev.creditapplicationsystem.controller

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.dto.CreditListView
import com.dgomesdev.creditapplicationsystem.dto.CreditView
import com.dgomesdev.creditapplicationsystem.service.CreditService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditService
) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        return try {
            creditService.saveCredit(creditDto)
            ResponseEntity.status(HttpStatus.CREATED).body("Credit saved successfully!")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error when saving credit")
        }
    }

    @GetMapping
    fun findAllCreditsByCustomerId(@RequestParam(value = "customerId") customerId: Long): List<CreditListView> {
        return try {
            creditService.findAllCreditsByCustomerId(customerId).map { credit -> CreditListView(credit) }
        //creditService.findAllCreditsByCustomerId(customerId).stream().map { credit -> CreditView(credit) }.collect(Collectors.toList())
        } catch (e: Exception) {
            emptyList()
        }
    }


    @GetMapping("/{creditCode}")
    fun findCreditByCreditCode(@RequestParam(value = "customerId") customerId: Long, @PathVariable creditCode: UUID): ResponseEntity<String> {
        return try {
            val credit = CreditView(creditService.findCreditByCreditCode(customerId, creditCode))
            ResponseEntity.status(HttpStatus.OK).body(
                "Credit code: ${credit.creditCode}" +
                "Credit value: ${credit.creditValue}" +
                "Number of installments: ${credit.numberOfInstallments}" +
                "Credit status: ${credit.status}" +
                "Customer's first name: ${credit.firstNameCustomer}" +
                "Customer's last name: ${credit.lastNameCustomer}"
            )
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error when loading the credit: ${e.message}")
        }
    }
}