package com.ran.kolibri.rest

import com.ran.kolibri.dto.request.user.ChangePasswordRequestDto
import com.ran.kolibri.dto.request.user.CreateUserRequestDto
import com.ran.kolibri.dto.request.user.EditUserRequestDto
import com.ran.kolibri.dto.request.user.LoginRequestDto
import com.ran.kolibri.dto.response.user.UserDto
import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.entity.user.UserStatus
import com.ran.kolibri.extension.map
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.security.JwtGenerator
import com.ran.kolibri.security.UserData
import com.ran.kolibri.security.annotation.AdminProtected
import com.ran.kolibri.service.user.UserService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtGenerator: JwtGenerator
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade

    @RequestMapping(value = "/login", method = arrayOf(POST))
    fun login(@RequestBody loginRequestDto: LoginRequestDto,
              response: HttpServletResponse): ResponseEntity<Any> {
        val user = userService.login(loginRequestDto.login!!, loginRequestDto.password!!)
        val userData = orikaMapperFacade.map<UserData>(user)
        jwtGenerator.encodeJwt(userData, response)
        return ResponseEntity(OK)
    }

    @RequestMapping(method = arrayOf(GET))
    fun getUsersPage(@RequestParam(value = "login", required = false) login: String?,
                     @RequestParam(value = "userStatus", required = false) userStatus: UserStatus?,
                     pageable: Pageable): Page<UserDto> {
        val usersPage = userService.getUsersPage(pageable, login, UserRole.ORDINARY_USER, userStatus)
        return orikaMapperFacade.mapAsPage(usersPage, pageable)
    }

    @RequestMapping(value = "/{id}", method = arrayOf(GET))
    fun getUserById(@PathVariable("id") id: Long): UserDto {
        val user = userService.getUserById(id)
        return orikaMapperFacade.map(user)
    }

    @RequestMapping(value = "/me", method = arrayOf(GET))
    fun getCurrentUser(): UserDto {
        val user = userService.getAuthenticatedUser()
        return orikaMapperFacade.map(user)
    }

    @RequestMapping(method = arrayOf(POST))
    @AdminProtected
    fun createUser(@RequestBody createUserRequestDto: CreateUserRequestDto): UserDto {
        val user = userService.createUser(createUserRequestDto.login!!)
        return orikaMapperFacade.map(user)
    }

    @RequestMapping(value = "/{id}", method = arrayOf(PUT))
    @AdminProtected
    fun editUserById(@PathVariable("id") id: Long,
                     @RequestBody editUserRequestDto: EditUserRequestDto): ResponseEntity<Any> {
        userService.editUserById(id, editUserRequestDto.userStatus!!)
        return ResponseEntity(OK)
    }

    @RequestMapping(value = "/change-password", method = arrayOf(POST))
    fun changePassword(@RequestBody changePasswordRequestDto: ChangePasswordRequestDto): ResponseEntity<Any> {
        userService.changePassword(changePasswordRequestDto.password!!)
        return ResponseEntity(OK)
    }

}
