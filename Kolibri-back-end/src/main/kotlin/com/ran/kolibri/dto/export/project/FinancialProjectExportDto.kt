package com.ran.kolibri.dto.export.project

import com.ran.kolibri.dto.export.financial.AccountExportDto
import com.ran.kolibri.dto.export.financial.FinancialProjectSettingsExportDto
import com.ran.kolibri.dto.export.financial.OperationCategoryExportDto
import com.ran.kolibri.dto.export.financial.OperationExportDto

class FinancialProjectExportDto : ProjectExportDto(projectType = FINANCIAL_PROJECT_TYPE) {

    var accounts: List<AccountExportDto>? = null
    var operations: List<OperationExportDto>? = null
    var operationCategories: List<OperationCategoryExportDto>? = null
    var settings: FinancialProjectSettingsExportDto? = null

}
