package com.ran.kolibri.component.util

import com.ran.kolibri.dto.request.common.CommentTextDto
import com.ran.kolibri.dto.request.common.CreateOrEditNamedEntityRequestDto
import com.ran.kolibri.dto.request.financial.*
import com.ran.kolibri.dto.request.project.CreateProjectRequestDto
import com.ran.kolibri.exception.BadRequestException
import org.springframework.stereotype.Component

@Component
// todo: replace this component by usage of JSON Schema Validator
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

    fun checkAddIncomeOperationRequestDto(addIncomeOperationDto: AddIncomeOperationRequestDto) {
        checkNotNull(addIncomeOperationDto.incomeAccountId, "Income Account")
        checkAddOperationRequestDto(addIncomeOperationDto)
    }

    fun checkAddExpendOperationRequestDto(addExpendOperationDto: AddExpendOperationRequestDto) {
        checkNotNull(addExpendOperationDto.expendAccountId, "Expend Account")
        checkAddOperationRequestDto(addExpendOperationDto)
    }

    fun checkAddTransferOperationRequestDto(addTransferOperationDto: AddTransferOperationRequestDto) {
        checkNotNull(addTransferOperationDto.fromAccountId, "From Account")
        checkNotNull(addTransferOperationDto.toAccountId, "To Account")
        checkDifferentIds(addTransferOperationDto.fromAccountId, addTransferOperationDto.toAccountId,
                "From Account", "To Account")
        checkAddOperationRequestDto(addTransferOperationDto)
    }

    fun checkAddOperationRequestDto(addOperationDto: AddOperationRequestDto) {
        checkNotNull(addOperationDto.operationCategoryId, "Operation Category")
        checkNotNull(addOperationDto.operationDate, "Operation date")
        checkNotZero(addOperationDto.moneyAmount, "Money amount")
        checkNotNull(addOperationDto.comment, "Comment")
    }

    fun checkEditOperationRequestDto(editOperationDto: EditOperationRequestDto) {
        checkNotNull(editOperationDto.operationCategoryId, "Operation Category")
        checkNotZero(editOperationDto.moneyAmount, "Money amount")
    }

    fun checkCreateProjectDto(createProjectDto: CreateProjectRequestDto) {
        checkNotNull(createProjectDto.name, "Name")
        checkNotNull(createProjectDto.description, "Description")
        checkNotNull(createProjectDto.isTemplate, "Is Template")
    }

    fun checkCreateOrEditNamedEntityDto(createOrEditNamedEntityDto: CreateOrEditNamedEntityRequestDto) {
        checkNotNull(createOrEditNamedEntityDto.name, "Name")
        checkNotNull(createOrEditNamedEntityDto.description, "Description")
    }

    fun checkEditAccountDto(editAccountRequestDto: EditAccountRequestDto) {
        checkCreateOrEditNamedEntityDto(editAccountRequestDto)
        checkNotNull(editAccountRequestDto.isActive, "Is Active")
    }

    fun checkCommentTextDto(commentTextDto: CommentTextDto) {
        checkNotNull(commentTextDto.text, "Text")
    }
    
}
