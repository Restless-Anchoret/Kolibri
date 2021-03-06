package com.ran.kolibri.entity.base

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull

@MappedSuperclass
abstract class NamedEntity(name: String, description: String) : BaseEntity() {

    @NotEmpty
    var name: String = name

    @NotNull
    var description: String = description

}
