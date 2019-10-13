package org.motivepick.web

import org.motivepick.security.JwtTokenFactory
import org.motivepick.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/temporary/login")
class TemporaryAccountController(private val config: ServerConfig, private val tokenFactory: JwtTokenFactory, private val userService: UserService) {

    @GetMapping
    fun login(request: HttpServletRequest, response: HttpServletResponse) {
        val temporaryAccountId = UUID.randomUUID().toString()
        userService.createUserWithTasksIfNotExists(temporaryAccountId, "", true)
        val token = tokenFactory.createAccessJwtToken(temporaryAccountId)
        val redirectUrl = if (request.getParameter("mobile") == null) config.authenticationSuccessUrlWeb else config.authenticationSuccessUrlMobile
        response.sendRedirect(redirectUrl + token)
    }
}
