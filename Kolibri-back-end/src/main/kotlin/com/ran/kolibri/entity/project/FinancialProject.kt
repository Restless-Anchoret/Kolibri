package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.entity.financial.OperationCategory
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "financial_project")
class FinancialProject : Project() {

    @OneToMany
    val accounts: MutableList<Account> = ArrayList()

    @OneToMany
    val operations: MutableList<Operation> = ArrayList()

    @OneToMany
    val operationCategories: MutableList<OperationCategory> = ArrayList()

}