package com.ran.kolibri.entity.property

import com.ran.kolibri.entity.base.BaseEntity
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Property : BaseEntity() {

    @NotEmpty
    var key: String = ""

    @NotNull
    var value: String = ""

}