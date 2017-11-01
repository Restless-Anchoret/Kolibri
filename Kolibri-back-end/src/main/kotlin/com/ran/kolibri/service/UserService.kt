package com.ran.kolibri.service

import com.ran.kolibri.entity.user.User
import com.ran.kolibri.entity.user.UserRole.UNKNOWN_USER
import com.ran.kolibri.entity.user.UserStatus.DISABLED
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.exception.UnauthorizedException
import com.ran.kolibri.repository.user.UserRepository
import com.ran.kolibri.security.BCryptPasswordEncoder
import com.ran.kolibri.security.UserData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService {

    companion object {
        private val DEFAULT_PASSWORD = "password"
    }

    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder
    @Autowired
    lateinit var userRepository: UserRepository

    fun getAuthenticatedUserData(): UserData {
        return SecurityContextHolder.getContext().authentication.principal as UserData
    }

    @Transactional
    fun getAuthenticatedUser(): User {
        return getUserById(getAuthenticatedUserData().id)
    }

    @Transactional
    fun getUserById(id: Long): User {
        return userRepository.getOne(id) ?: throw NotFoundException("User not found")
    }

    @Transactional
    fun login(login: String, password: String): User {
        val user = userRepository.findByLogin(login)
        if (user == null || user.userRole == UNKNOWN_USER) {
            throw UnauthorizedException("User with the given login does not exist")
        }
        if (user.userStatus == DISABLED) {
            throw UnauthorizedException("User with the given login is disabled")
        }
        if (!passwordEncoder.checkPassword(password, user.passwordHash)) {
            throw UnauthorizedException("Incorrect password")
        }
        return user
    }

    @Transactional
    fun createUser(login: String): User {
        val alreadyExistingUser = userRepository.findByLogin(login)
        if (alreadyExistingUser != null) {
            throw BadRequestException("User the given login already exists")
        }
        val passwordHash = passwordEncoder.encodePassword(DEFAULT_PASSWORD)
        val user = User(login, passwordHash)
        userRepository.save(user)
        return user
    }

}
