package com.ran.kolibri.rest

import com.ran.kolibri.component.DtoPropertyChecker
import com.ran.kolibri.dto.financial.AccountDTO
import com.ran.kolibri.dto.financial.OperationCategoryDTO
import com.ran.kolibri.dto.financial.OperationDTO
import com.ran.kolibri.dto.project.FinancialProjectDTO
import com.ran.kolibri.dto.request.AddExpendOperationRequestDTO
import com.ran.kolibri.dto.request.AddIncomeOperationRequestDTO
import com.ran.kolibri.dto.request.AddTransferOperationRequestDTO
import com.ran.kolibri.dto.request.EditOperationRequestDTO
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.service.AccountService
import com.ran.kolibri.service.OperationCategoryService
import com.ran.kolibri.service.OperationService
import com.ran.kolibri.service.ProjectService
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
    lateinit var projectService: ProjectService
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
    fun getFinancialProjectById(@PathVariable("projectId") projectId: Long): FinancialProjectDTO {
        val project = projectService.getFinancialProjectById(projectId)
        return orikaMapperFacade.map(project, FinancialProjectDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts"), method = arrayOf(GET))
    fun getFinancialProjectAccounts(@PathVariable("projectId") projectId: Long,
                                    @RequestParam(value = "name", required = false) name: String?,
                                    pageable: Pageable): Page<AccountDTO> {
        val accountsPage = accountService.getAccountsByProjectId(projectId, name, pageable)
        return orikaMapperFacade.mapAsPage(accountsPage, pageable, AccountDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories"), method = arrayOf(GET))
    fun getFinancialProjectOperationCategories(@PathVariable("projectId") projectId: Long,
                                               @RequestParam(value = "name", required = false) name: String?,
                                               pageable: Pageable): Page<OperationCategoryDTO> {
        val operationCategoriesPage = operationCategoryService.getOperationCategoriesByProjectId(projectId, name, pageable)
        return orikaMapperFacade.mapAsPage(operationCategoriesPage, pageable, OperationCategoryDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations"), method = arrayOf(GET))
    fun getFinancialProjectOperations(@PathVariable("projectId") projectId: Long,
                                      pageable: Pageable): Page<OperationDTO> {
        val operationsPage = operationService.getOperationsByProjectId(projectId, pageable)
        return orikaMapperFacade.mapAsPage(operationsPage, pageable, OperationDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/income"), method = arrayOf(POST))
    fun addIncomeOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addIncomeOperationDto: AddIncomeOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddIncomeOperationRequestDto(addIncomeOperationDto)
        operationService.addIncomeOperation(projectId,
                addIncomeOperationDto.incomeAccountId!!, addIncomeOperationDto.operationCategoryId!!,
                addIncomeOperationDto.moneyAmount!!, addIncomeOperationDto.comment!!,
                addIncomeOperationDto.operationDate!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/expend"), method = arrayOf(POST))
    fun addExpendOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addExpendOperationDto: AddExpendOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddExpendOperationRequestDto(addExpendOperationDto)
        operationService.addExpendOperation(projectId,
                addExpendOperationDto.expendAccountId!!, addExpendOperationDto.operationCategoryId!!,
                addExpendOperationDto.moneyAmount!!, addExpendOperationDto.comment!!,
                addExpendOperationDto.operationDate!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations/transfer"), method = arrayOf(POST))
    fun addTransferOperation(@PathVariable("projectId") projectId: Long,
                             @RequestBody addTransferOperationDto: AddTransferOperationRequestDTO): ResponseEntity<Any> {
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
                      @RequestBody editOperationDto: EditOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkEditOperationRequestDto(editOperationDto)
        operationService.editOperation(operationId, editOperationDto.operationCategoryId!!,
                editOperationDto.moneyAmount!!, editOperationDto.comment!!)
        return ResponseEntity(HttpStatus.OK)
    }

}
