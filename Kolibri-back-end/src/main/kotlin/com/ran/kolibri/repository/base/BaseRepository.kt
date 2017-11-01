package com.ran.kolibri.repository.base

import com.ran.kolibri.entity.base.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseRepository<T: BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>
