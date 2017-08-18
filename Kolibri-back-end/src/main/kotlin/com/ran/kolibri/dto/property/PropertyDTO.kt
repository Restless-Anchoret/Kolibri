package com.ran.kolibri.dto.property

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.BaseEntityDTO
import com.ran.kolibri.dto.property.PropertyDTO.Companion.GLOBAL_PROPERTY_TYPE
import com.ran.kolibri.dto.property.PropertyDTO.Companion.PROJECT_PROPERTY_TYPE

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = ProjectPropertyDTO::class, name = PROJECT_PROPERTY_TYPE),
        JsonSubTypes.Type(value = GlobalPropertyDTO::class, name = GLOBAL_PROPERTY_TYPE))
abstract class PropertyDTO(
        var propertyType: String? = null,
        var key: String? = null,
        var value: String? = null
) : BaseEntityDTO() {

    companion object {
        const val PROJECT_PROPERTY_TYPE = "project"
        const val GLOBAL_PROPERTY_TYPE = "global"
    }

}