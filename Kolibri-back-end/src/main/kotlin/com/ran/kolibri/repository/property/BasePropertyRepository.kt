package com.ran.kolibri.repository.property

import com.ran.kolibri.entity.property.Property
import org.springframework.data.repository.CrudRepository

interface BasePropertyRepository<T: Property> : CrudRepository<T, Long>