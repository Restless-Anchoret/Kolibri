package com.ran.kolibri.rest

import com.ran.kolibri.component.DtoPropertyChecker
import com.ran.kolibri.component.OrikaMapperFacadeFactory
import com.ran.kolibri.dto.financial.AccountDTO
import com.ran.kolibri.dto.financial.OperationCategoryDTO
import com.ran.kolibri.dto.financial.OperationDTO
import com.ran.kolibri.dto.project.ProjectDTO
import com.ran.kolibri.dto.request.AddExpendOperationRequestDTO
import com.ran.kolibri.dto.request.AddIncomeOperationRequestDTO
import com.ran.kolibri.dto.request.AddTransferOperationRequestDTO
import com.ran.kolibri.service.AccountService
import com.ran.kolibri.service.OperationCategoryService
import com.ran.kolibri.service.OperationService
import com.ran.kolibri.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController {

    @Autowired
    lateinit var projectService: ProjectService
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

    @RequestMapping(method = arrayOf(GET))
    fun getAllActiveProjects(): List<ProjectDTO> {
        val projects = projectService.getAllActiveProjects()
        return orikaMapperFacadeFactory.`object`.mapAsList(projects, ProjectDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/templates"), method = arrayOf(GET))
    fun getAllTemplateProjects(): List<ProjectDTO> {
        val projects = projectService.getAllTemplateProjects()
        return orikaMapperFacadeFactory.`object`.mapAsList(projects, ProjectDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/accounts"), method = arrayOf(GET))
    fun getFinancialProjectAccounts(@PathVariable("projectId") projectId: Long): List<AccountDTO> {
        val accounts = accountService.getAllAccountsByProjectId(projectId)
        return orikaMapperFacadeFactory.`object`.mapAsList(accounts, AccountDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/categories"), method = arrayOf(GET))
    fun getFinancialProjectOperationCategories(@PathVariable("projectId") projectId: Long): List<OperationCategoryDTO> {
        val operationCategories = operationCategoryService.getAllOperationCategoriesByProjectId(projectId)
        return orikaMapperFacadeFactory.`object`.mapAsList(operationCategories, OperationCategoryDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/operations"), method = arrayOf(GET))
    fun getFinancialProjectOperations(@PathVariable("projectId") projectId: Long): List<OperationDTO> {
        val operations = operationService.getAllOperationsByProjectId(projectId)
        return orikaMapperFacadeFactory.`object`.mapAsList(operations, OperationDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/{projectId}/income"), method = arrayOf(POST))
    fun addIncomeOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addIncomeOperationDto: AddIncomeOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddIncomeOperationRequestDto(addIncomeOperationDto)
        operationService.addIncomeOperation(projectId,
                addIncomeOperationDto.incomeAccountId!!, addIncomeOperationDto.operationCategoryId!!,
                addIncomeOperationDto.moneyAmount!!, addIncomeOperationDto.comment!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/expend"), method = arrayOf(POST))
    fun addExpandOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addExpendOperationDto: AddExpendOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddExpendOperationRequestDto(addExpendOperationDto)
        operationService.addExpendOperation(projectId,
                addExpendOperationDto.expendAccountId!!, addExpendOperationDto.operationCategoryId!!,
                addExpendOperationDto.moneyAmount!!, addExpendOperationDto.comment!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/transfer"), method = arrayOf(POST))
    fun addTransferOperation(@PathVariable("projectId") projectId: Long,
                           @RequestBody addTransferOperationDto: AddTransferOperationRequestDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkAddTransferOperationRequestDto(addTransferOperationDto)
        operationService.addTransferOperation(projectId,
                addTransferOperationDto.fromAccountId!!, addTransferOperationDto.toAccountId!!,
                addTransferOperationDto.operationCategoryId!!, addTransferOperationDto.moneyAmount!!,
                addTransferOperationDto.comment!!)
        return ResponseEntity(HttpStatus.OK)
    }

}