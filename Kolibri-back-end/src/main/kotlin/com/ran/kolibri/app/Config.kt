package com.ran.kolibri.app

object Config {

    // JDBC properties
    const val JDBC_URL_PROD = "\${jdbc.url.prod}"
    const val JDBC_URL_DEV = "\${jdbc.url.dev}"
    const val JDBC_URL_LIQUIBASE_UPDATE = "\${jdbc.url.liquibase-update}"
    const val JDBC_USERNAME = "\${jdbc.username}"
    const val JDBC_PASSWORD = "\${jdbc.password}"
    const val JDBC_DRIVER = "\${jdbc.driver}"
    const val JDBC_USE_UNICODE = "\${jdbc.use-unicode}"
    const val JDBC_CHARACTER_ENCODING = "\${jdbc.character-encoding}"

    // Hibernate properties
    const val HIBERNATE_DIALECT = "\${hibernate.dialect}"
    const val HIBERNATE_SHOW_SQL = "\${hibernate.show_sql}"
    const val HIBERNATE_HBM2DDL_AUTO = "\${hibernate.hbm2ddl.auto}"

    // VCS properties
    const val VCS_COMMIT_AUTHOR = "\${vcs.commit-author}"
    const val VCS_COMMIT_EMAIL = "\${vcs.commit-email}"
    const val VCS_COMMIT_MESSAGE = "\${vcs.commit-message}"

    // App directory properties
    const val APP_DIRECTORY_ROOT = "\${app-directory.root}"

    // Scheduling tasks properties
    const val SCHEDULING_GIT_COMMIT_TASK_PERIOD = "\${scheduling.git-commit-task.period}"

    // JWT properties
    const val JWT_EXPIRE_DAYS = "\${jwt.expire-days}"
    const val JWT_REFRESH_DAYS = "\${jwt.refresh-days}"
    const val JWT_SECRET = "\${jwt.secret}"
    const val JWT_IGNORED_URLS = "\${jwt.ignored-urls}"

}
