package com.ran.kolibri.dto.property

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.BaseEntityDto
import com.ran.kolibri.dto.property.PropertyDto.Companion.GLOBAL_PROPERTY_TYPE
import com.ran.kolibri.dto.property.PropertyDto.Companion.PROJECT_PROPERTY_TYPE

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = ProjectPropertyDto::class, name = PROJECT_PROPERTY_TYPE),
        JsonSubTypes.Type(value = GlobalPropertyDto::class, name = GLOBAL_PROPERTY_TYPE))
abstract class PropertyDto(
        var propertyType: String? = null,
        var key: String? = null,
        var value: String? = null
) : BaseEntityDto() {

    companion object {
        const val PROJECT_PROPERTY_TYPE = "project"
        const val GLOBAL_PROPERTY_TYPE = "global"
    }

}
