package com.ran.kolibri.dto.request

data class CreateProjectRequestDTO(
        var name: String? = null,
        var description: String? = null,
        var isTemplate: Boolean? = null
)
