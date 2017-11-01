package com.ran.kolibri.security

import com.ran.kolibri.app.Config.JWT_EXPIRE_DAYS
import com.ran.kolibri.app.Config.JWT_REFRESH_DAYS
import com.ran.kolibri.app.Config.JWT_SECRET
import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.entity.user.UserRole.UNKNOWN_USER
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClaims
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtGenerator {

    companion object {
        private val AUTHORIZATION = "Authorization"
        private val BEARER_PREFIX = "Bearer "
        private val ID = "id"
        private val LOGIN = "login"
        private val USER_ROLE = "user_role"
        private val EXPIRE = "expire"
        private val REFRESH = "refresh"
        private val DAY_SECONDS = (60 * 60 * 24).toLong()
    }

    @Value(JWT_EXPIRE_DAYS)
    private var expireDays: Int = 0
    @Value(JWT_REFRESH_DAYS)
    private var refreshDays: Int = 0
    @Value(JWT_SECRET)
    lateinit var secret: String

    fun encodeJwt(userData: UserData, response: HttpServletResponse) {
        val expire = Date.from(Instant.now().plusSeconds(DAY_SECONDS * expireDays)).time
        val refresh = Date.from(Instant.now().plusSeconds(DAY_SECONDS * refreshDays)).time

        val tokenData = HashMap<String, Any>()
        tokenData.put(ID, userData.id.toString())
        tokenData.put(LOGIN, userData.login)
        tokenData.put(USER_ROLE, userData.userRole.toString())
        tokenData.put(EXPIRE, expire.toString())
        tokenData.put(REFRESH, refresh.toString())

        val jwtBuilder = Jwts.builder()
        jwtBuilder.setClaims(tokenData)

        val token = jwtBuilder.signWith(SignatureAlgorithm.HS256, secret).compact()
        response.setHeader(AUTHORIZATION, BEARER_PREFIX + token)
    }

    fun decodeJwt(request: HttpServletRequest): DecodeResult {
        val token = extractToken(request) ?: return DecodeResult(isSuccess = false)
        val claims = extractClaims(token) ?: return DecodeResult(isSuccess = false)

        val expire = Date(claims.get(EXPIRE, String::class.java).toLong()).toInstant()
        val refresh = Date(claims.get(REFRESH, String::class.java).toLong()).toInstant()
        if (expire.isBefore(Instant.now())) {
            return DecodeResult(isSuccess = false)
        }

        val id = claims.get(ID, String::class.java).toLong()
        val login = claims.get(LOGIN, String::class.java)
        val userRole = UserRole.valueOf(claims.get(USER_ROLE, String::class.java))
        val userData = UserData(
                id = id,
                login = login,
                userRole = userRole)
        return DecodeResult(userData, expire, refresh)
    }

    private fun extractToken(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader(AUTHORIZATION)
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null
        }

        val token = authorizationHeader.substring(BEARER_PREFIX.length)
        return if (token.isEmpty()) {
            null
        } else token
    }

    private fun extractClaims(token: String): DefaultClaims? {
        return try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parse(token).body as DefaultClaims
        } catch (ex: Exception) {
            null
        }
    }

    class DecodeResult(
            val userData: UserData = UserData(0, "", UNKNOWN_USER),
            val expire: Instant = Instant.now(),
            val refresh: Instant = Instant.now(),
            val isSuccess: Boolean = true
    )

}
