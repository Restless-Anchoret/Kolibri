package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.property.ProjectProperty
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.OneToMany
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Project : NamedEntity() {

    @OneToMany
    val projectProperties: List<ProjectProperty> = ArrayList()

    @NotNull
    var isTemplate: Boolean = false

}