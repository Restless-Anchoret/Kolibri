package com.ran.kolibri.extension

import ma.glasnost.orika.MapperFacade
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

fun <S, D> MapperFacade.mapAsPage(page: Page<S>, pageable: Pageable, destinationClass: Class<D>): Page<D> {
    val convertedContent = mapAsList(page.content, destinationClass)
    return PageImpl(convertedContent, pageable, page.totalElements)
}
