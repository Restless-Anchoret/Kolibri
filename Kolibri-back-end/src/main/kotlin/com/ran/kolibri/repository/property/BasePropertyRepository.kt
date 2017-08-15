package com.ran.kolibri.repository.property

import com.ran.kolibri.entity.property.Property
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BasePropertyRepository<T: Property> : CrudRepository<T, Long>