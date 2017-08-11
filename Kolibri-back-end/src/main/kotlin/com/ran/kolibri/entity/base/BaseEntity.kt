package com.ran.kolibri.entity.base

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class BaseEntity {

    @Id
    @GeneratedValue
    var id: Long? = null

}