package com.ran.kolibri.app

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType.SWAGGER_2
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.ApiInfo

@Configuration
@EnableWebSecurity
class WebConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
    }

    @Bean
    fun swaggerApi(): Docket {
        return Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ran.kolibri.rest"))
                .paths(PathSelectors.any())
                .build().apiInfo(metadata())
    }

    private fun metadata(): ApiInfo {
        return ApiInfoBuilder()
                .title("Kolibri REST API")
                .description("Provides the API to control Kolibri")
                .version("1.0")
                .build()
    }

}
