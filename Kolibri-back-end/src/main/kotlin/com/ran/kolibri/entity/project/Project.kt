package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.property.ProjectProperty
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Project : NamedEntity() {

    @OneToMany(mappedBy = "project")
    val projectProperties: List<ProjectProperty> = ArrayList()

    @NotNull
    var isTemplate: Boolean = false

}
