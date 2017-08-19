package com.ran.kolibri.dto.project

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.NamedEntityDTO
import com.ran.kolibri.dto.project.ProjectDTO.Companion.FINANCIAL_PROJECT_TYPE

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "projectType")
@JsonSubTypes(
        JsonSubTypes.Type(value = FinancialProjectDTO::class, name = FINANCIAL_PROJECT_TYPE))
abstract class ProjectDTO(
        var isTemplate: Boolean? = null,
        var projectType: String? = null
) : NamedEntityDTO() {

    companion object {
        const val FINANCIAL_PROJECT_TYPE = "financial"
    }

}
