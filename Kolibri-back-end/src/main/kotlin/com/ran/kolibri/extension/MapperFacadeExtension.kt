package com.ran.kolibri.extension

import ma.glasnost.orika.MapperFacade
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

inline fun <reified T> MapperFacade.mapAsPage(page: Page<*>, pageable: Pageable): Page<T> {
    val convertedContent = mapAsList(page.content, T::class.java)
    return PageImpl(convertedContent, pageable, page.totalElements)
}

inline fun <reified T> MapperFacade.map(obj: Any): T {
    return map(obj, T::class.java)
}

inline fun <reified T> MapperFacade.mapAsList(list: List<*>): List<T> {
    return mapAsList(list, T::class.java)
}
