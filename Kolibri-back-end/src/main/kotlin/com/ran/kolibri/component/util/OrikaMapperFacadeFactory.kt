package com.ran.kolibri.component.util

import com.ran.kolibri.dto.export.financial.*
import com.ran.kolibri.dto.export.project.FinancialProjectExportDto
import com.ran.kolibri.dto.response.financial.ExpendOperationDto
import com.ran.kolibri.dto.response.financial.IncomeOperationDto
import com.ran.kolibri.dto.response.financial.TransferOperationDto
import com.ran.kolibri.dto.response.project.FinancialProjectDto
import com.ran.kolibri.entity.financial.*
import com.ran.kolibri.entity.project.FinancialProject
import ma.glasnost.orika.MapperFacade
import ma.glasnost.orika.MapperFactory
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.stereotype.Component

@Component
class OrikaMapperFacadeFactory : FactoryBean<MapperFacade> {

    override fun getObject(): MapperFacade {
        val mapperFactory = DefaultMapperFactory.Builder().build()
        createMappings(mapperFactory)
        return mapperFactory.mapperFacade
    }

    override fun getObjectType(): Class<MapperFacade> {
        return MapperFacade::class.java
    }

    override fun isSingleton(): Boolean {
        return true
    }

    private fun createMappings(mapperFactory: MapperFactory) {
        // Financial Project DTO classes
        mapperFactory.classMap(FinancialProject::class.java, FinancialProjectDto::class.java).byDefault().register()
        mapperFactory.classMap(IncomeOperation::class.java, IncomeOperationDto::class.java).byDefault().register()
        mapperFactory.classMap(ExpendOperation::class.java, ExpendOperationDto::class.java).byDefault().register()
        mapperFactory.classMap(TransferOperation::class.java, TransferOperationDto::class.java).byDefault().register()

        // Financial Project Export DTO classes
        mapperFactory.classMap(FinancialProject::class.java, FinancialProjectExportDto::class.java).byDefault().register()
        mapperFactory.classMap(FinancialProjectSettings::class.java, FinancialProjectSettingsExportDto::class.java)
                .field("defaultAccount.id", "defaultAccountId")
                .field("defaultOperationCategory.id", "defaultOperationCategoryId")
                .byDefault().register()
        mapperFactory.classMap(Operation::class.java, OperationExportDto::class.java)
                .field("operationCategory.id", "operationCategoryId")
                .byDefault().register()
        mapperFactory.classMap(IncomeOperation::class.java, IncomeOperationExportDto::class.java)
                .field("incomeAccount.id", "incomeAccountId")
                .byDefault().register()
        mapperFactory.classMap(ExpendOperation::class.java, ExpendOperationExportDto::class.java)
                .field("expendAccount.id", "expendAccountId")
                .byDefault().register()
        mapperFactory.classMap(TransferOperation::class.java, TransferOperationExportDto::class.java)
                .field("fromAccount.id", "fromAccountId")
                .field("toAccount.id", "toAccountId")
                .byDefault().register()
    }

}
