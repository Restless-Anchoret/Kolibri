package com.ran.kolibri.app

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {

    @Bean
    fun dataSource(@Value("\${jdbc.url}") url: String,
                   @Value("\${jdbc.username}") username: String,
                   @Value("\${jdbc.password}") password: String,
                   @Value("\${jdbc.driver}") driverClassName: String): DataSource {
        val dataSource = DriverManagerDataSource(url, username, password)
        dataSource.setDriverClassName(driverClassName)
        return dataSource
    }

}