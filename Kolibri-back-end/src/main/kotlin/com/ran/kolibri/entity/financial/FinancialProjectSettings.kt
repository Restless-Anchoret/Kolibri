package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.project.FinancialProject
import org.jetbrains.annotations.NotNull
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class FinancialProjectSettings: BaseEntity() {

    @NotNull
    @OneToOne
    var financialProject: FinancialProject? = null

    @OneToOne
    var defaultAccount: Account? = null

    @OneToOne
    var defaultOperationCategory: OperationCategory? = null

}
