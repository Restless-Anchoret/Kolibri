package com.ran.kolibri.entity.project

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Project {

    @Id
    @GeneratedValue
    var id: Long? = null

    var name: String? = null
    var description: String? = null

}