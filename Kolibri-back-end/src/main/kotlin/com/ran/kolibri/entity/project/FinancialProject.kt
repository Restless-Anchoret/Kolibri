package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.FinancialProjectSettings
import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.entity.financial.OperationCategory
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
class FinancialProject : Project() {

    @OneToMany(mappedBy = "project")
    val accounts: MutableList<Account> = ArrayList()

    @OneToMany(mappedBy = "project")
    val operations: MutableList<Operation> = ArrayList()

    @OneToMany(mappedBy = "project")
    val operationCategories: MutableList<OperationCategory> = ArrayList()

    @OneToOne(mappedBy = "financialProject")
    var settings: FinancialProjectSettings? = null

}
