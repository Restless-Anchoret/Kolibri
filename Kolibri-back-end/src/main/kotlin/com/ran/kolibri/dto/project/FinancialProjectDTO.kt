package com.ran.kolibri.dto.project

import com.ran.kolibri.dto.financial.FinancialProjectSettingsDTO

class FinancialProjectDTO : ProjectDTO(projectType = FINANCIAL_PROJECT_TYPE) {

    var settings: FinancialProjectSettingsDTO? = null

}
