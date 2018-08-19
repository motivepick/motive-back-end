package org.motivepick.security

import org.motivepick.domain.entity.User
import org.motivepick.repository.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MotiveAuthenticationSuccessHandler(@Value("\${authentication.success.url}")
                                         private val authenticationSuccessUrl: String,

                                         private val userRepo: UserRepository)
    : SavedRequestAwareAuthenticationSuccessHandler() {

    init {
        isAlwaysUseDefaultTargetUrl = true
        defaultTargetUrl = authenticationSuccessUrl
    }

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        if (authentication is OAuth2AuthenticationToken) {
            val accountId = getAccountId(authentication)
            if (!userRepo.existsByAccountId(accountId)) {
                // TODO: do we need to store access tokens?
                val user = User(accountId, getUserName(authentication))
                userRepo.save(user)
            }
        }
        super.onAuthenticationSuccess(request, response, authentication)
    }

    private fun getAccountId(authentication: OAuth2AuthenticationToken): Long {
        return authentication.principal.attributes["id"]!!.toString().toLong()
    }

    private fun getUserName(authentication: OAuth2AuthenticationToken): String {
        return authentication.principal.attributes["name"]!!.toString()
    }
}