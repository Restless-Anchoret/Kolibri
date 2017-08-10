package com.ran.kolibri.app

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = arrayOf("com.ran.kolibri"))
class Application

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}