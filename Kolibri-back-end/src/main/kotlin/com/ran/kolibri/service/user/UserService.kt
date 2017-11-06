package com.ran.kolibri.service.user

import com.ran.kolibri.entity.user.User
import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.entity.user.UserRole.ADMIN
import com.ran.kolibri.entity.user.UserRole.UNKNOWN_USER
import com.ran.kolibri.entity.user.UserStatus
import com.ran.kolibri.entity.user.UserStatus.DISABLED
import com.ran.kolibri.exception.ForbiddenException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.exception.UnauthorizedException
import com.ran.kolibri.repository.user.UserRepository
import com.ran.kolibri.security.BCryptPasswordEncoder
import com.ran.kolibri.security.UserData
import com.ran.kolibri.specification.user.UserSpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
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
    fun getAdminUser(): User {
        return getUsersPage(pageable = PageRequest(0, 1),
                userRole = ADMIN).content.first()
    }

    @Transactional
    fun getUnknownUser(): User {
        return getUsersPage(pageable = PageRequest(0, 1),
                userRole = UNKNOWN_USER).content.first()
    }

    @Transactional
    fun getUsersPage(pageable: Pageable,
                     login: String? = null,
                     userRole: UserRole? = null,
                     userStatus: UserStatus? = null): Page<User> {
        val specification = Specifications.where(UserSpecificationFactory.byLogin(login))
                .and(UserSpecificationFactory.byUserRole(userRole))
                .and(UserSpecificationFactory.byUserStatus(userStatus))
        return userRepository.findAll(specification, pageable)
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
            throw ForbiddenException("User the given login already exists")
        }
        val passwordHash = passwordEncoder.encodePassword(DEFAULT_PASSWORD)
        val user = User(login, passwordHash)
        userRepository.save(user)
        return user
    }

    @Transactional
    fun editUserById(id: Long, userStatus: UserStatus) {
        if (id == getAuthenticatedUserData().id) {
            throw ForbiddenException("It is forbidden to edit status of the authenticated User")
        }
        val user = getUserById(id)
        user.userStatus = userStatus
        userRepository.save(user)
    }

    @Transactional
    fun changePassword(password: String) {
        val user = getAuthenticatedUser()
        user.passwordHash = passwordEncoder.encodePassword(password)
        userRepository.save(user)
    }

}
