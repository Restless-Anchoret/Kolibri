package com.ran.kolibri.specification.financial

import com.ran.kolibri.entity.financial.*
import com.ran.kolibri.entity.project.FinancialProject
import org.springframework.data.jpa.domain.Specification
import java.util.*

object OperationSpecificationFactory {

    fun byProjectId(projectId: Long?): Specification<Operation>? {
        return if (projectId == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<FinancialProject>("project").get<Long>("id"), projectId)
        }
    }

    fun <T: Operation> afterDateAndId(date: Date, operationId: Long?): Specification<T>? {
        return Specification { root, _, criteriaBuilder ->
            if (operationId == null) {
                criteriaBuilder.greaterThan(root.get<Date>("operationDate"), date)
            } else {
                criteriaBuilder.or(
                        criteriaBuilder.greaterThan(root.get<Date>("operationDate"), date),
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get<Date>("operationDate"), date),
                                criteriaBuilder.greaterThanOrEqualTo(root.get<Long>("id"), operationId)
                        )
                )
            }
        }
    }

    fun byIncomeAccount(incomeAccountId: Long): Specification<IncomeOperation> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Account>("incomeAccount").get<Long>("id"), incomeAccountId)
        }
    }

    fun byExpendAccount(expendAccountId: Long): Specification<ExpendOperation> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Account>("expendAccount").get<Long>("id"), expendAccountId)
        }
    }

    fun byTransferFromAccount(fromAccountId: Long): Specification<TransferOperation> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Account>("fromAccount").get<Long>("id"), fromAccountId)
        }
    }

    fun byTransferToAccount(toAccountId: Long): Specification<TransferOperation> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Account>("toAccount").get<Long>("id"), toAccountId)
        }
    }

}
