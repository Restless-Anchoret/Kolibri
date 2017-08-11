package com.ran.kolibri.app

enum class ApplicationProfile (
        val profileName: String
) {

    PROD("prod"),
    DEBUG("debug"),
    TEST("test"),
    LIQUIBASE_UPDATE("liquibase_update")

}