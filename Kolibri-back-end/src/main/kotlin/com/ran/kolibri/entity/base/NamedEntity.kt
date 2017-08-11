package com.ran.kolibri.entity.base

import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class NamedEntity : BaseEntity() {

    @NotEmpty
    var name: String = ""

    @NotNull
    var description: String = ""

}