package com.ran.kolibri.repository.specification

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate

object CommonSpecificationFactory {

    fun <T> byName(name: String): Specification<T> {
        return Specification { root, _, criteriaBuilder ->
            createLikePredicate(criteriaBuilder, root.get<String>("name"), name)
        }
    }

    fun createLikePredicate(criteriaBuilder: CriteriaBuilder, expression: Expression<String>,
                            likeValue: String): Predicate {
        return criteriaBuilder.like(criteriaBuilder.lower(expression), likeValue.toLowerCase())
    }

}
