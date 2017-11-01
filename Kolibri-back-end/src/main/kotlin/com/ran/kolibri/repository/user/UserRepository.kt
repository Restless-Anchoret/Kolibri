package com.ran.kolibri.repository.user

import com.ran.kolibri.entity.user.User
import com.ran.kolibri.repository.base.BaseRepository

interface UserRepository : BaseRepository<User> {

    fun findByLogin(login: String): User?

}
