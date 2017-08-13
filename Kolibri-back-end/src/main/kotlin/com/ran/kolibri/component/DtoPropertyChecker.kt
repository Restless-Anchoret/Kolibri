package com.ran.kolibri.component

import com.ran.kolibri.dto.request.AddExpendOperationRequestDTO
import com.ran.kolibri.dto.request.AddIncomeOperationRequestDTO
import com.ran.kolibri.dto.request.AddOperationRequestDTO
import com.ran.kolibri.dto.request.AddTransferOperationRequestDTO
import com.ran.kolibri.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
class DtoPropertyChecker {
    
    fun <T> checkProperty(property: T?, propertyName: String) {
        property ?: throw BadRequestException("Property $propertyName must be not null")
    }

    fun checkAddIncomeOperationRequestDto(addIncomeOperationDto: AddIncomeOperationRequestDTO) {
        checkProperty(addIncomeOperationDto.incomeAccountId, "incomeAccountId")
        checkAddOperationRequestDto(addIncomeOperationDto)
    }

    fun checkAddExpendOperationRequestDto(addExpendOperationDto: AddExpendOperationRequestDTO) {
        checkProperty(addExpendOperationDto.expendAccountId, "expendAccountId")
        checkAddOperationRequestDto(addExpendOperationDto)
    }

    fun checkAddTransferOperationRequestDto(addTransferOperationDto: AddTransferOperationRequestDTO) {
        checkProperty(addTransferOperationDto.fromAccountId, "fromAccountId")
        checkProperty(addTransferOperationDto.toAccountId, "toAccountId")
        checkAddOperationRequestDto(addTransferOperationDto)
    }

    fun checkAddOperationRequestDto(addOperationDto: AddOperationRequestDTO) {
        checkProperty(addOperationDto.operationCategoryId, "operationCategoryId")
        checkProperty(addOperationDto.moneyAmount, "moneyAmount")
        checkProperty(addOperationDto.comment, "comment")
    }
    
}