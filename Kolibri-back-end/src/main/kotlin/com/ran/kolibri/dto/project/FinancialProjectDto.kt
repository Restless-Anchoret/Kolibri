package com.ran.kolibri.dto.project

import com.ran.kolibri.dto.financial.FinancialProjectSettingsDto

class FinancialProjectDto : ProjectDto(projectType = FINANCIAL_PROJECT_TYPE) {

    var settings: FinancialProjectSettingsDto? = null

}
