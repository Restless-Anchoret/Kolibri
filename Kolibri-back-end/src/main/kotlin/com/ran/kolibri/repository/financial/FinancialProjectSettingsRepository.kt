package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.FinancialProjectSettings
import org.springframework.data.repository.CrudRepository

interface FinancialProjectSettingsRepository : CrudRepository<FinancialProjectSettings, Long>
