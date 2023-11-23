package com.dgomesdev.creditapplicationsystem.controller

import com.dgomesdev.creditapplicationsystem.dto.CreditDto
import com.dgomesdev.creditapplicationsystem.dto.CreditListView
import com.dgomesdev.creditapplicationsystem.dto.CreditView
import com.dgomesdev.creditapplicationsystem.service.impl.CreditServiceImpl
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/credits")
class CreditResource(
    private val creditService: CreditServiceImpl
) {

    @PostMapping
    fun saveCredit(@RequestBody @Valid creditDto: CreditDto): ResponseEntity<String> {
        creditService.saveCredit(creditDto)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body("Credit of customer with ID ${creditDto.customerId} saved successfully!")

    }

    @GetMapping
    fun findAllCreditsByCustomerId(@RequestParam(value = "customerId") customerId: Long): List<CreditListView> {
        return creditService.findAllCreditsByCustomerId(customerId).map { credit -> CreditListView(credit) }
        //creditService.findAllCreditsByCustomerId(customerId).stream().map { credit -> CreditView(credit) }.collect(Collectors.toList())
    }

    @GetMapping("/{creditCode}")
    fun findCreditByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID
    ): ResponseEntity<String> {
        val credit = CreditView(creditService.findCreditByCreditCode(customerId, creditCode))
        return ResponseEntity.status(HttpStatus.OK).body(
            "Credit code: ${credit.creditCode} \n" +
                    "Credit value: ${credit.creditValue} \n" +
                    "Number of installments: ${credit.numberOfInstallments} \n" +
                    "Credit status: ${credit.status} \n" +
                    "Customer's first name: ${credit.firstNameCustomer} \n" +
                    "Customer's last name: ${credit.lastNameCustomer} \n"
        )
    }
}