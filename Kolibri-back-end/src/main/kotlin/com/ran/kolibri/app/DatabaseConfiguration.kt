package com.ran.kolibri.app

import com.ran.kolibri.app.ApplicationProfile.DEV
import com.ran.kolibri.app.ApplicationProfile.LIQUIBASE_UPDATE
import com.ran.kolibri.app.ApplicationProfile.PROD
import com.ran.kolibri.app.Config.HIBERNATE_DIALECT
import com.ran.kolibri.app.Config.HIBERNATE_HBM2DDL_AUTO
import com.ran.kolibri.app.Config.HIBERNATE_SHOW_SQL
import com.ran.kolibri.app.Config.JDBC_CHARACTER_ENCODING
import com.ran.kolibri.app.Config.JDBC_DRIVER
import com.ran.kolibri.app.Config.JDBC_PASSWORD
import com.ran.kolibri.app.Config.JDBC_URL_DEV
import com.ran.kolibri.app.Config.JDBC_URL_LIQUIBASE_UPDATE
import com.ran.kolibri.app.Config.JDBC_URL_PROD
import com.ran.kolibri.app.Config.JDBC_USERNAME
import com.ran.kolibri.app.Config.JDBC_USE_UNICODE
import liquibase.integration.spring.SpringLiquibase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import java.util.*
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {

    companion object {
        private val USE_UNICODE_PROPERTY = "useUnicode"
        private val CHARACTER_ENCODING_PROPERTY = "characterEncoding"
    }

    @Autowired
    lateinit var environment: Environment

    @Bean
    @Profile(PROD, DEV, LIQUIBASE_UPDATE)
    fun dataSource(
            @Value(JDBC_URL_PROD) urlProd: String,
            @Value(JDBC_URL_DEV) urlDev: String,
            @Value(JDBC_URL_LIQUIBASE_UPDATE) urlLiquibaseUpdate: String,
            @Value(JDBC_USERNAME) username: String,
            @Value(JDBC_PASSWORD) password: String,
            @Value(JDBC_DRIVER) driverClassName: String,
            @Value(JDBC_USE_UNICODE) useUnicode: String,
            @Value(JDBC_CHARACTER_ENCODING) characterEncoding: String): DataSource {
        val profiles = environment.activeProfiles

        val url = when {
            profiles.contains(PROD) -> urlProd
            profiles.contains(DEV) -> urlDev
            profiles.contains(LIQUIBASE_UPDATE) -> urlLiquibaseUpdate
            else -> ""
        }

        val dataSource = DriverManagerDataSource(url, username, password)
        dataSource.setDriverClassName(driverClassName)

        val properties = Properties()
        properties.put(USE_UNICODE_PROPERTY, useUnicode)
        properties.put(CHARACTER_ENCODING_PROPERTY, characterEncoding)
        dataSource.connectionProperties = properties

        return dataSource
    }

    @Bean
    @Autowired
    fun sessionFactory(@Value(HIBERNATE_DIALECT) dialect: String,
                       @Value(HIBERNATE_SHOW_SQL) showSql: String,
                       @Value(HIBERNATE_HBM2DDL_AUTO) hbm2ddl: String,
                       dataSource: DataSource): LocalSessionFactoryBean {
        val hibernateProperties = Properties()
        hibernateProperties["hibernate.dialect"] = dialect
        hibernateProperties["hibernate.show_sql"] = showSql
        hibernateProperties["hibernate.hbm2ddl.auto"] = hbm2ddl
        hibernateProperties["hibernate.format_sql"] = true

        val sessionFactory = LocalSessionFactoryBean()
        sessionFactory.setDataSource(dataSource)
        sessionFactory.setPackagesToScan("com.ran.kolibri.entity")
        sessionFactory.hibernateProperties = hibernateProperties
        return sessionFactory
    }

    @Bean
    @Autowired
    @Profile(PROD, DEV)
    fun liquibase(dataSource: DataSource): SpringLiquibase {
        val liquibase = SpringLiquibase()
        liquibase.changeLog = "classpath:liquibase/db-change-log.xml"
        liquibase.dataSource = dataSource
        return liquibase
    }

}
