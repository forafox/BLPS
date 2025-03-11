package com.jellyone.blps.web.security.jaas

import com.sun.security.auth.UserPrincipal
import javax.security.auth.spi.LoginModule
import javax.security.auth.Subject

class CustomLoginModule : LoginModule {
    private lateinit var subject: Subject
    private var username: String? = null
    private var password: String? = null
    private var authenticated = false

    override fun initialize(
        subject: Subject,
        callbackHandler: javax.security.auth.callback.CallbackHandler?,
        sharedState: MutableMap<String, *>?,
        options: MutableMap<String, *>?
    ) {
        this.subject = subject
    }

    override fun login(): Boolean {
        // Здесь обращение к БД для проверки пользователя
        if (username == "admin" && password == "admin") {
            authenticated = true
        }
        return authenticated
    }

    override fun commit(): Boolean {
        if (authenticated) {
            subject.principals.add(UserPrincipal(username!!))
            return true
        }
        return false
    }

    override fun abort(): Boolean = false
    override fun logout(): Boolean = true
}
