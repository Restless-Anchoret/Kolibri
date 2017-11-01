package com.ran.kolibri.dto.response.base

abstract class NamedEntityDto : BaseEntityDto() {

    var name: String? = null
    var description: String? = null

}