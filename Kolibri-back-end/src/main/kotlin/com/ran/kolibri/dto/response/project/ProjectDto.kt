package com.ran.kolibri.dto.response.project

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.response.base.NamedEntityDto
import com.ran.kolibri.dto.response.comment.CommentDto
import com.ran.kolibri.dto.response.project.ProjectDto.Companion.FINANCIAL_PROJECT_TYPE

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "projectType")
@JsonSubTypes(
        JsonSubTypes.Type(value = FinancialProjectDto::class, name = FINANCIAL_PROJECT_TYPE))
abstract class ProjectDto(
        var isTemplate: Boolean? = null,
        var projectType: String? = null,
        var comments: List<CommentDto>? = null
) : NamedEntityDto() {

    companion object {
        const val FINANCIAL_PROJECT_TYPE = "financial"
        const val FINANCIAL_EXPORT_PROJECT_TYPE = "financialExport"
    }

}
