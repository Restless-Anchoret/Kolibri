package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.repository.base.BaseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface AccountRepository : BaseRepository<Account> {

    @Query("select op " +
            "from Operation op " +
            "where (op.id in (" +
                    "select incOp.id " +
                    "from IncomeOperation incOp " +
                    "where incOp.incomeAccount.id = :accountId" +
                ") or op.id in (" +
                    "select expOp.id " +
                    "from ExpendOperation expOp " +
                    "where expOp.expendAccount.id = :accountId" +
                ") or op.id in (" +
                    "select trOp.id " +
                    "from TransferOperation trOp " +
                    "where trOp.fromAccount.id = :accountId or " +
                        "trOp.toAccount.id = :accountId" +
                ")) and " +
                "op.operationDate <= :date " +
            "order by op.operationDate desc, op.id desc")
    fun findAccountOperationsBeforeDate(@Param("accountId") accountId: Long,
                                        @Param("date") date: Date,
                                        pageable: Pageable = PageRequest(0, 1)): Page<Operation>

    @Query("select op " +
            "from Operation op " +
            "where (op.id in (" +
                    "select incOp.id " +
                    "from IncomeOperation incOp " +
                    "where incOp.incomeAccount.id = :accountId" +
                ") or op.id in (" +
                    "select expOp.id " +
                    "from ExpendOperation expOp " +
                    "where expOp.expendAccount.id = :accountId" +
                ") or op.id in (" +
                    "select trOp.id " +
                    "from TransferOperation trOp " +
                    "where trOp.fromAccount.id = :accountId or " +
                        "trOp.toAccount.id = :accountId" +
                ")) " +
            "order by op.operationDate desc, op.id desc")
    fun findAllAccountOperations(@Param("accountId") accountId: Long,
                                 pageable: Pageable = PageRequest(0, 1)): Page<Operation>

}
