package com.ran.kolibri.app

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
@EnableWebSecurity
class WebConfiguration : WebSecurityConfigurerAdapter() {

//    @Bean
//    fun corsConfigurer(): WebMvcConfigurer {
//        return object : WebMvcConfigurerAdapter() {
//            override fun addCorsMappings(registry: CorsRegistry) {
//                registry.addMapping("/*")
//                        .allowedOrigins("/*")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE")
//            }
//        }
//    }

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
    }

}