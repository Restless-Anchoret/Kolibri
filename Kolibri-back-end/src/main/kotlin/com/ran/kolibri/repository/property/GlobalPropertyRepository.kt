package com.ran.kolibri.repository.property

import com.ran.kolibri.entity.property.GlobalProperty
import org.springframework.stereotype.Repository

@Repository
interface GlobalPropertyRepository : BasePropertyRepository<GlobalProperty> {

    fun findByKey(key: String): GlobalProperty?

}