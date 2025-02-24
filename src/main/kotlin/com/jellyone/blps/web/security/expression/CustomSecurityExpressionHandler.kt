package com.jellyone.blps.web.security.expression

import com.jellyone.blps.service.UserService
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;


class CustomSecurityExpressionHandler : DefaultMethodSecurityExpressionHandler() {

    private lateinit var applicationContext: ApplicationContext
    private val trustResolver: AuthenticationTrustResolver = AuthenticationTrustResolverImpl()

    override fun createSecurityExpressionRoot(
        authentication: Authentication,
        invocation: MethodInvocation
    ): MethodSecurityExpressionOperations {

        val root = CustomMethodSecurityExpressionRoot(authentication).apply {
            setTrustResolver(trustResolver)
            setPermissionEvaluator(permissionEvaluator)
            setRoleHierarchy(roleHierarchy)
            setUserService(applicationContext.getBean(UserService::class.java))
        }
        return root
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        super.setApplicationContext(applicationContext)
        this.applicationContext = applicationContext
    }
}