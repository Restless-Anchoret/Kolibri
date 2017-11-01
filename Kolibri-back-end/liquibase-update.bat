set PGPASSWORD=postgres
psql postgresql://localhost:5432/kolibri_database_liquibase_update kolibri_user < drop-schema.sql
del src\main\resources\liquibase\db-change-log-create.xml
call mvn clean package
call mvn spring-boot:start -Drun.arguments="--hibernate.hbm2ddl.auto=create,--liquibase.enabled=false,--spring.profiles.active=liquibase_update"
call mvn liquibase:generateChangeLog
