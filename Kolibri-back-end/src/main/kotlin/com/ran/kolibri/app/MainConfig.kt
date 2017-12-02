package com.ran.kolibri.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.orm.jpa.EntityScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.ran.kolibri"))
@EnableJpaRepositories(basePackages = arrayOf("com.ran.kolibri.repository"))
@EntityScan(basePackages = arrayOf("com.ran.kolibri.entity"))
@EnableTransactionManagement
@EnableScheduling
@EnableSwagger2
class MainConfig
