package com.jellyone.blps.configuration

import com.atomikos.jdbc.AtomikosDataSourceBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
class DataSourceConfig {
    @Bean(name = ["primaryDataSource"])
    @Primary
    fun primaryDataSource(): DataSource {
            val dataSource = AtomikosDataSourceBean()
            dataSource.uniqueResourceName = "primary-db"
            dataSource.xaDataSourceClassName = "org.postgresql.xa.PGXADataSource"
            dataSource.xaProperties.setProperty("serverName", "localhost")
            dataSource.xaProperties.setProperty("portNumber", "5432")
            dataSource.xaProperties.setProperty("databaseName", "postgres")
            dataSource.xaProperties.setProperty("user", "postgres")
            dataSource.xaProperties.setProperty("password", "postgres")
            return dataSource
        }
}