package com.ran.kolibri.component

import com.ran.kolibri.dto.project.FinancialProjectDTO
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
        mapperFactory.classMap(FinancialProject::class.java, FinancialProjectDTO::class.java).byDefault().register()
    }

}