package com.ran.kolibri.entity.base

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull

@MappedSuperclass
abstract class NamedEntity : BaseEntity() {

    @NotEmpty
    var name: String = ""

    @NotNull
    var description: String = ""

}