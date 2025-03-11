package com.jellyone.blps.configuration


import com.atomikos.icatch.jta.UserTransactionImp
import com.atomikos.icatch.jta.UserTransactionManager
import jakarta.transaction.TransactionManager
import jakarta.transaction.UserTransaction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.jta.JtaTransactionManager

@Configuration
class TransactionManagerConfig {
    @Bean(name = ["atomikosTransactionManager"])
    @Primary
    fun atomikosTransactionManager(): TransactionManager {
        val userTransactionManager = UserTransactionManager()
        userTransactionManager.forceShutdown = false
        return userTransactionManager
    }

    @Bean(name = ["userTransaction"])
    @Primary
    fun userTransaction(): UserTransaction {
        val userTransactionImp = UserTransactionImp()
        userTransactionImp.setTransactionTimeout(5)
        return userTransactionImp
    }

    @Bean(name = ["transactionManager"])
    @DependsOn("userTransaction", "atomikosTransactionManager")
    @Primary
    fun transactionManager(): PlatformTransactionManager {
        val userTransaction = userTransaction()
        val atomikosTransactionManager = atomikosTransactionManager()
        return JtaTransactionManager(userTransaction, atomikosTransactionManager)
    }
}