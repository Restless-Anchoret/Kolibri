package com.ran.kolibri.dto.property

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.BaseEntityDTO

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = ProjectPropertyDTO::class, name = "project"),
        JsonSubTypes.Type(value = GlobalPropertyDTO::class, name = "global"))
abstract class PropertyDTO : BaseEntityDTO() {

    var key: String? = null
    var value: String? = null

}