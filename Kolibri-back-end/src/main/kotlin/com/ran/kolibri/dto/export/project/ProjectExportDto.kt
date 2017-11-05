package com.ran.kolibri.dto.export.project

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.export.base.NamedEntityExportDto
import com.ran.kolibri.dto.export.comment.CommentExportDto
import com.ran.kolibri.dto.export.project.ProjectExportDto.Companion.FINANCIAL_PROJECT_TYPE

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "projectType")
@JsonSubTypes(
        JsonSubTypes.Type(value = FinancialProjectExportDto::class, name = FINANCIAL_PROJECT_TYPE))
abstract class ProjectExportDto(
        var projectType: String
) : NamedEntityExportDto() {

    companion object {
        const val FINANCIAL_PROJECT_TYPE = "financial"
    }

    var isTemplate: Boolean? = null
    var comments: List<CommentExportDto>? = null

}
