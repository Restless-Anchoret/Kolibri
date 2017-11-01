package com.ran.kolibri.rest

import com.ran.kolibri.dto.request.CreateUserRequestDto
import com.ran.kolibri.dto.request.LoginRequestDto
import com.ran.kolibri.dto.response.user.UserDto
import com.ran.kolibri.extension.map
import com.ran.kolibri.security.JwtGenerator
import com.ran.kolibri.security.UserData
import com.ran.kolibri.service.UserService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var jwtGenerator: JwtGenerator
    @Autowired
    lateinit var mapperFacade: MapperFacade

    @RequestMapping(value = "/login", method = arrayOf(POST))
    fun login(@RequestBody loginRequestDto: LoginRequestDto,
              response: HttpServletResponse): ResponseEntity<Any> {
        val user = userService.login(loginRequestDto.login!!, loginRequestDto.password!!)
        val userData = mapperFacade.map<UserData>(user)
        jwtGenerator.encodeJwt(userData, response)
        return ResponseEntity(OK)
    }

    @RequestMapping(method = arrayOf(GET))
    fun getUsersPage() {

    }

    @RequestMapping(value = "/{id}", method = arrayOf(GET))
    fun getUser() {

    }

    @RequestMapping(method = arrayOf(POST))
    fun createUser(@RequestBody createUserRequestDto: CreateUserRequestDto): UserDto {
        val user = userService.createUser(createUserRequestDto.login!!)
        return mapperFacade.map(user)
    }

    @RequestMapping(value = "/{id}", method = arrayOf(PUT))
    fun editUser() {

    }

}
