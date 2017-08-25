package com.ran.kolibri.specification.base

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate

object BaseSpecificationFactory {

    fun <T> byName(name: String?): Specification<T>? {
        return if (name == null) null else Specification { root, _, criteriaBuilder ->
            createLikePredicate(criteriaBuilder, root.get<String>("name"), name)
        }
    }

    fun createLikePredicate(criteriaBuilder: CriteriaBuilder, expression: Expression<String>,
                            likeValue: String): Predicate {
        return criteriaBuilder.like(criteriaBuilder.lower(expression), "%${likeValue.toLowerCase()}%")
    }

}
