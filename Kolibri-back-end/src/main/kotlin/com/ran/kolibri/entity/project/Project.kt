package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.base.NamedEntity
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Project : NamedEntity() {

    @NotNull
    var isTemplate: Boolean = false

}
