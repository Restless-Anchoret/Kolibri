package com.ran.kolibri.dto.project

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.NamedEntityDTO

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = FinancialProjectDTO::class, name = "financial"))
abstract class ProjectDTO : NamedEntityDTO() {

    var isTemplate: Boolean? = null

}