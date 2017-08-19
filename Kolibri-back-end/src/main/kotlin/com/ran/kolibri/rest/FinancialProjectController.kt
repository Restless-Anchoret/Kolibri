package com.ran.kolibri.rest

import com.ran.kolibri.component.DtoPropertyChecker
import com.ran.kolibri.component.OrikaMapperFacadeFactory
import com.ran.kolibri.dto.financial.AccountDTO
import com.ran.kolibri.dto.financial.OperationCategoryDTO
import com.ran.kolibri.dto.financial.OperationDTO
import com.ran.kolibri.dto.request.AddExpendOperationRequestDTO
import com.ran.kolibri.dto.request.AddIncomeOperationRequestDTO
import com.ran.kolibri.dto.request.AddTransferOperationRequestDTO
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.service.AccountService
import com.ran.kolibri.service.OperationCategoryService
import com.ran.kolibri.service.OperationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/financial-projects")
class FinancialProjectController {

    @Autowired
    lateinit var operationCategoryService: OperationCategoryService
    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var operationService: OperationService
    @Autowired
    lateinit var orikaMapperFacadeFactory: OrikaMapperFacadeFactory
    @Autowired
    lateinit var dtoPropertyChecker: DtoPropertyChecker

    @RequestMapping(path = arrayOf("/{projectId}/accounts"), method = arrayOf(RequestMethod.GET))
    fun getFinancialProjectAccounts(@PathVariable("projectId") projectId: Long): List<AccountDTO> {
        val accounts = accountService.getAllAccountsByProjectId(projectId)
        return orikaMapperFacadeFactory.`object`.mapAsList(accounts, AccountDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories"), method = arrayOf(RequestMethod.GET))
    fun getFinancialProjectOperationCategories(@PathVariable("projectId") projectId: Long): List<OperationCategoryDTO> {
        val operationCategories = operationCategoryService.getAllOperationCategoriesByProjectId(projectId)
        return orikaMapperFacadeFactory.`object`.mapAsList(operationCategories, OperationCategoryDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations"), method = arrayOf(RequestMethod.GET))
    fun getFinancialProjectOperations(@PathVariable("projectId") projectId: Long,
                                      pageable: Pageable): Page<OperationDTO> {
        val operationsPage = operationService.getAllOperationsByProjectId(projectId, pageable)
        return orikaMapperFacadeFactory.`object`.mapAsPage(operationsPage, pageable, OperationDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/income"), method = arrayOf(RequestMethod.POST))
    fun addIncomeOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addIncomeOperationDto: AddIncomeOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddIncomeOperationRequestDto(addIncomeOperationDto)
        operationService.addIncomeOperation(projectId,
                addIncomeOperationDto.incomeAccountId!!, addIncomeOperationDto.operationCategoryId!!,
                addIncomeOperationDto.moneyAmount!!, addIncomeOperationDto.comment!!,
                addIncomeOperationDto.operationDate)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/expend"), method = arrayOf(RequestMethod.POST))
    fun addExpandOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addExpendOperationDto: AddExpendOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddExpendOperationRequestDto(addExpendOperationDto)
        operationService.addExpendOperation(projectId,
                addExpendOperationDto.expendAccountId!!, addExpendOperationDto.operationCategoryId!!,
                addExpendOperationDto.moneyAmount!!, addExpendOperationDto.comment!!,
                addExpendOperationDto.operationDate)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/transfer"), method = arrayOf(RequestMethod.POST))
    fun addTransferOperation(@PathVariable("projectId") projectId: Long,
                             @RequestBody addTransferOperationDto: AddTransferOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddTransferOperationRequestDto(addTransferOperationDto)
        operationService.addTransferOperation(projectId,
                addTransferOperationDto.fromAccountId!!, addTransferOperationDto.toAccountId!!,
                addTransferOperationDto.operationCategoryId!!, addTransferOperationDto.moneyAmount!!,
                addTransferOperationDto.comment!!, addTransferOperationDto.operationDate)
        return ResponseEntity(HttpStatus.OK)
    }

}
