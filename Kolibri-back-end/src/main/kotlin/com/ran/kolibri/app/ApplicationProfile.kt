package com.ran.kolibri.app

enum class ApplicationProfile (
        val profileName: String
) {

    PROD("prod"),
    DEV("dev"),
    TEST("test"),
    LIQUIBASE_UPDATE("liquibase_update")

}