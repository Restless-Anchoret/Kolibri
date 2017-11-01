package com.ran.kolibri.rest

import com.ran.kolibri.component.DtoPropertyChecker
import com.ran.kolibri.dto.financial.AccountDto
import com.ran.kolibri.dto.financial.OperationCategoryDto
import com.ran.kolibri.dto.financial.OperationDto
import com.ran.kolibri.dto.project.FinancialProjectDto
import com.ran.kolibri.dto.request.*
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.service.AccountService
import com.ran.kolibri.service.FinancialProjectService
import com.ran.kolibri.service.OperationCategoryService
import com.ran.kolibri.service.OperationService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/financial-projects")
class FinancialProjectController {

    @Autowired
    lateinit var financialProjectService: FinancialProjectService
    @Autowired
    lateinit var operationCategoryService: OperationCategoryService
    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var operationService: OperationService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade
    @Autowired
    lateinit var dtoPropertyChecker: DtoPropertyChecker

    @RequestMapping(path = arrayOf("/{projectId}"), method = arrayOf(GET))
    fun getFinancialProjectById(@PathVariable("projectId") projectId: Long): FinancialProjectDto {
        val project = financialProjectService.getFinancialProjectById(projectId)
        return orikaMapperFacade.map(project, FinancialProjectDto::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/settings"), method = arrayOf(PUT))
    fun setFinancialProjectSettings(@PathVariable("projectId") projectId: Long,
                                    @RequestBody setFinancialProjectSettingsRequestDto:
                                    SetFinancialProjectSettingsRequestDto): FinancialProjectDto {
        val project = financialProjectService.setFinancialProjectSettings(projectId,
                setFinancialProjectSettingsRequestDto.accountId,
                setFinancialProjectSettingsRequestDto.operationCategoryId)
        return orikaMapperFacade.map(project, FinancialProjectDto::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts"), method = arrayOf(GET))
    fun getFinancialProjectAccounts(@PathVariable("projectId") projectId: Long,
                                    @RequestParam(value = "name", required = false) name: String?,
                                    pageable: Pageable): Page<AccountDto> {
        val accountsPage = accountService.getAccountsByProjectId(projectId, name, pageable)
        return orikaMapperFacade.mapAsPage(accountsPage, pageable)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories"), method = arrayOf(GET))
    fun getFinancialProjectOperationCategories(@PathVariable("projectId") projectId: Long,
                                               @RequestParam(value = "name", required = false) name: String?,
                                               pageable: Pageable): Page<OperationCategoryDto> {
        val operationCategoriesPage = operationCategoryService.getOperationCategoriesByProjectId(projectId, name, pageable)
        return orikaMapperFacade.mapAsPage(operationCategoriesPage, pageable)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations"), method = arrayOf(GET))
    fun getFinancialProjectOperations(@PathVariable("projectId") projectId: Long,
                                      pageable: Pageable): Page<OperationDto> {
        val operationsPage = operationService.getOperationsByProjectId(projectId, pageable)
        return orikaMapperFacade.mapAsPage(operationsPage, pageable)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/income"), method = arrayOf(POST))
    fun addIncomeOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addIncomeOperationDto: AddIncomeOperationRequestDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddIncomeOperationRequestDto(addIncomeOperationDto)
        operationService.addIncomeOperation(projectId,
                addIncomeOperationDto.incomeAccountId!!, addIncomeOperationDto.operationCategoryId!!,
                addIncomeOperationDto.moneyAmount!!, addIncomeOperationDto.comment!!,
                addIncomeOperationDto.operationDate!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/expend"), method = arrayOf(POST))
    fun addExpendOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addExpendOperationDto: AddExpendOperationRequestDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddExpendOperationRequestDto(addExpendOperationDto)
        operationService.addExpendOperation(projectId,
                addExpendOperationDto.expendAccountId!!, addExpendOperationDto.operationCategoryId!!,
                addExpendOperationDto.moneyAmount!!, addExpendOperationDto.comment!!,
                addExpendOperationDto.operationDate!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/transfer"), method = arrayOf(POST))
    fun addTransferOperation(@PathVariable("projectId") projectId: Long,
                             @RequestBody addTransferOperationDto: AddTransferOperationRequestDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddTransferOperationRequestDto(addTransferOperationDto)
        operationService.addTransferOperation(projectId,
                addTransferOperationDto.fromAccountId!!, addTransferOperationDto.toAccountId!!,
                addTransferOperationDto.operationCategoryId!!, addTransferOperationDto.moneyAmount!!,
                addTransferOperationDto.comment!!, addTransferOperationDto.operationDate!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/{operationId}"), method = arrayOf(DELETE))
    fun removeOperation(@PathVariable("operationId") operationId: Long): ResponseEntity<Any> {
        operationService.removeOperation(operationId)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/{operationId}"), method = arrayOf(PUT))
    fun editOperation(@PathVariable("operationId") operationId: Long,
                      @RequestBody editOperationDto: EditOperationRequestDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkEditOperationRequestDto(editOperationDto)
        operationService.editOperation(operationId, editOperationDto.operationCategoryId!!,
                editOperationDto.moneyAmount!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts/create"), method = arrayOf(POST))
    fun createAccount(@PathVariable("projectId") projectId: Long,
                      @RequestBody createNamedEntityRequestDto: CreateOrEditNamedEntityRequestDto): AccountDto {
        dtoPropertyChecker.checkCreateOrEditNamedEntityDto(createNamedEntityRequestDto)
        val account = accountService.createAccount(projectId,
                createNamedEntityRequestDto.name!!, createNamedEntityRequestDto.description!!)
        return orikaMapperFacade.map(account, AccountDto::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts/{accountId}"), method = arrayOf(PUT))
    fun editAccount(@PathVariable("accountId") accountId: Long,
                    @RequestBody editAccountRequestDto: EditAccountRequestDto): AccountDto {
        dtoPropertyChecker.checkEditAccountDto(editAccountRequestDto)
        val account = accountService.editAccount(accountId, editAccountRequestDto.name!!,
                editAccountRequestDto.description!!, editAccountRequestDto.isActive!!)
        return orikaMapperFacade.map(account, AccountDto::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts/{accountId}"), method = arrayOf(DELETE))
    fun removeAccount(@PathVariable("accountId") accountId: Long): ResponseEntity<Any> {
        accountService.removeAccount(accountId)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories/create"), method = arrayOf(POST))
    fun createOperationCategory(@PathVariable("projectId") projectId: Long,
                                @RequestBody createNamedEntityRequestDto: CreateOrEditNamedEntityRequestDto)
                                    : OperationCategoryDto {
        dtoPropertyChecker.checkCreateOrEditNamedEntityDto(createNamedEntityRequestDto)
        val operationCategory = operationCategoryService.createOperationCategory(projectId,
                createNamedEntityRequestDto.name!!, createNamedEntityRequestDto.description!!)
        return orikaMapperFacade.map(operationCategory, OperationCategoryDto::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories/{categoryId}"), method = arrayOf(PUT))
    fun editOperationCategory(@PathVariable("categoryId") operationCategoryId: Long,
                              @RequestBody createNamedEntityRequestDto: CreateOrEditNamedEntityRequestDto)
                                : OperationCategoryDto {
        dtoPropertyChecker.checkCreateOrEditNamedEntityDto(createNamedEntityRequestDto)
        val operationCategory = operationCategoryService.editOperationCategory(operationCategoryId,
                createNamedEntityRequestDto.name!!, createNamedEntityRequestDto.description!!)
        return orikaMapperFacade.map(operationCategory, OperationCategoryDto::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories/{categoryId}"), method = arrayOf(DELETE))
    fun removeOperationCategory(@PathVariable("categoryId") operationCategoryId: Long): ResponseEntity<Any> {
        operationCategoryService.removeOperationCategory(operationCategoryId)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/{operationId}/comment"), method = arrayOf(POST))
    fun addOperationComment(@PathVariable("operationId") operationId: Long,
                            @RequestBody commentTextDto: CommentTextDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        operationService.addOperationComment(operationId, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/{operationId}/comment/{commentIndex}"), method = arrayOf(PUT))
    fun editOperationComment(@PathVariable("operationId") operationId: Long,
                             @PathVariable("commentIndex") commentIndex: Int,
                             @RequestBody commentTextDto: CommentTextDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        operationService.editOperationComment(operationId, commentIndex, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/{operationId}/comment/{commentIndex}"), method = arrayOf(DELETE))
    fun removeOperationComment(@PathVariable("operationId") operationId: Long,
                               @PathVariable("commentIndex") commentIndex: Int): ResponseEntity<Any> {
        operationService.removeOperationComment(operationId, commentIndex)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts/{accountId}/comment"), method = arrayOf(POST))
    fun addAccountComment(@PathVariable("accountId") accountId: Long,
                          @RequestBody commentTextDto: CommentTextDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        accountService.addAccountComment(accountId, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts/{accountId}/comment/{commentIndex}"), method = arrayOf(PUT))
    fun editAccountComment(@PathVariable("accountId") accountId: Long,
                           @PathVariable("commentIndex") commentIndex: Int,
                           @RequestBody commentTextDto: CommentTextDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        accountService.editAccountComment(accountId, commentIndex, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts/{accountId}/comment/{commentIndex}"), method = arrayOf(DELETE))
    fun removeAccountComment(@PathVariable("accountId") accountId: Long,
                             @PathVariable("commentIndex") commentIndex: Int): ResponseEntity<Any> {
        accountService.removeAccountComment(accountId, commentIndex)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories/{categoryId}/comment"), method = arrayOf(POST))
    fun addOperationCategoryComment(@PathVariable("categoryId") categoryId: Long,
                                    @RequestBody commentTextDto: CommentTextDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        operationCategoryService.addOperationCategoryComment(categoryId, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories/{categoryId}/comment/{commentIndex}"), method = arrayOf(PUT))
    fun editOperationCategoryComment(@PathVariable("categoryId") categoryId: Long,
                                     @PathVariable("commentIndex") commentIndex: Int,
                                     @RequestBody commentTextDto: CommentTextDto): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        operationCategoryService.editOperationCategoryComment(categoryId, commentIndex, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories/{categoryId}/comment/{commentIndex}"), method = arrayOf(DELETE))
    fun removeOperationCategoryComment(@PathVariable("categoryId") categoryId: Long,
                                       @PathVariable("commentIndex") commentIndex: Int): ResponseEntity<Any> {
        operationCategoryService.removeOperationCategoryComment(categoryId, commentIndex)
        return ResponseEntity(HttpStatus.OK)
    }

}
