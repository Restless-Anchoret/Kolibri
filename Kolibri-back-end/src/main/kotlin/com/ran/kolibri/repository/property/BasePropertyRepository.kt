package com.ran.kolibri.repository.property

import com.ran.kolibri.entity.property.Property
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BasePropertyRepository<T: Property> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>
