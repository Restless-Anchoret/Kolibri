package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.entity.financial.OperationCategory
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class FinancialProject : Project() {

    @OneToMany
    val accounts: List<Account> = ArrayList()

    @OneToMany
    val operations: List<Operation> = ArrayList()

    @OneToMany
    val operationCategories: List<OperationCategory> = ArrayList()

}