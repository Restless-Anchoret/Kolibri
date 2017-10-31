package com.ran.kolibri.dto.request

data class CreateProjectRequestDto(
        var name: String? = null,
        var description: String? = null,
        var isTemplate: Boolean? = null
)
