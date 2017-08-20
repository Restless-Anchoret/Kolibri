package com.ran.kolibri.entity.property

import com.ran.kolibri.entity.project.Project
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class ProjectProperty : Property() {

    @NotNull
    @ManyToOne
    var project: Project? = null

}
