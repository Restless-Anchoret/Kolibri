package com.ran.kolibri.component

import com.ran.kolibri.dto.request.*
import com.ran.kolibri.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
class DtoPropertyChecker {
    
    fun <T> checkNotNull(property: T?, propertyName: String) {
        property ?: throw BadRequestException("$propertyName must be not null")
    }

    fun checkNotZero(doubleProperty: Double?, propertyName: String) {
        checkNotNull(doubleProperty, propertyName)
        if (Math.abs(doubleProperty!!) < 1e-8) {
            throw BadRequestException("$propertyName must not be zero")
        }
    }

    fun checkDifferentIds(firstId: Long?, secondId: Long?,
                          firstPropertyName: String, secondPropertyName: String) {
        if (firstId == secondId) {
            throw BadRequestException("$firstPropertyName and $secondPropertyName must be different")
        }
    }

    fun checkAddIncomeOperationRequestDto(addIncomeOperationDto: AddIncomeOperationRequestDTO) {
        checkNotNull(addIncomeOperationDto.incomeAccountId, "Income Account")
        checkAddOperationRequestDto(addIncomeOperationDto)
    }

    fun checkAddExpendOperationRequestDto(addExpendOperationDto: AddExpendOperationRequestDTO) {
        checkNotNull(addExpendOperationDto.expendAccountId, "Expend Account")
        checkAddOperationRequestDto(addExpendOperationDto)
    }

    fun checkAddTransferOperationRequestDto(addTransferOperationDto: AddTransferOperationRequestDTO) {
        checkNotNull(addTransferOperationDto.fromAccountId, "From Account")
        checkNotNull(addTransferOperationDto.toAccountId, "To Account")
        checkDifferentIds(addTransferOperationDto.fromAccountId, addTransferOperationDto.toAccountId,
                "From Account", "To Account")
        checkAddOperationRequestDto(addTransferOperationDto)
    }

    fun checkAddOperationRequestDto(addOperationDto: AddOperationRequestDTO) {
        checkNotNull(addOperationDto.operationCategoryId, "Operation Category")
        checkNotNull(addOperationDto.operationDate, "Operation date")
        checkNotZero(addOperationDto.moneyAmount, "Money amount")
        checkNotNull(addOperationDto.comment, "Comment")
    }

    fun checkEditOperationRequestDto(editOperationDto: EditOperationRequestDTO) {
        checkNotNull(editOperationDto.operationCategoryId, "Operation Category")
        checkNotZero(editOperationDto.moneyAmount, "Money amount")
    }

    fun checkCreateProjectDto(createProjectDto: CreateProjectRequestDTO) {
        checkNotNull(createProjectDto.name, "Name")
        checkNotNull(createProjectDto.description, "Description")
        checkNotNull(createProjectDto.isTemplate, "Is Template")
    }

    fun checkCreateOrEditNamedEntityDto(createOrEditNamedEntityDto: CreateOrEditNamedEntityRequestDTO) {
        checkNotNull(createOrEditNamedEntityDto.name, "Name")
        checkNotNull(createOrEditNamedEntityDto.description, "Description")
    }

    fun checkEditAccountDto(editAccountRequestDTO: EditAccountRequestDTO) {
        checkCreateOrEditNamedEntityDto(editAccountRequestDTO)
        checkNotNull(editAccountRequestDTO.isActive, "Is Active")
    }

    fun checkCommentTextDto(commentTextDto: CommentTextDTO) {
        checkNotNull(commentTextDto.text, "Text")
    }
    
}
