package com.ran.kolibri.dto.response.project

import com.ran.kolibri.dto.response.financial.FinancialProjectSettingsDto

class FinancialProjectDto : ProjectDto(projectType = FINANCIAL_PROJECT_TYPE) {

    var settings: FinancialProjectSettingsDto? = null

}
