package com.ran.kolibri.security

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Component
class BCryptPasswordEncoder {

    fun encodePassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun checkPassword(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }

}
