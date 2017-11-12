package com.ran.kolibri.specification.base

import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate

object BaseSpecificationFactory {

    fun <T> empty(): Specification<T> {
        return Specification { _, _, builder ->
            builder.isTrue(builder.literal(false))
        }
    }

    fun <T> byInIds(ids: List<Long>): Specification<T> {
        return if (ids.isEmpty()) empty() else Specification { root, _, _ ->
            root.get<Long>("id").`in`(ids)
        }
    }

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
