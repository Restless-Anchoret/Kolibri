package com.ran.kolibri.entity.project

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.comment.Comment
import com.ran.kolibri.entity.comment.CommentsHolder
import com.ran.kolibri.entity.user.User
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.InheritanceType.JOINED
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = JOINED)
abstract class Project(
        name: String = "",
        description: String = "",
        @NotNull
        var isTemplate: Boolean = false
) : NamedEntity(name, description), CommentsHolder {

    @NotNull
    @ManyToOne
    var owner: User? = null

    @ManyToMany
    val usersWithAccess: MutableList<User> = ArrayList()

    @OneToMany(cascade = arrayOf(PERSIST, REMOVE))
    @OrderColumn
    override val comments: MutableList<Comment> = ArrayList()

}
