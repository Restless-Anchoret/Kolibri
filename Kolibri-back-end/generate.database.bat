del src\main\resources\liquibase\db-change-log-create.xml
mvn spring-boot:run -Drun.arguments="--hibernate.hbm2ddl.auto=create,--liquibase.enabled=false,--spring.profiles.active=liquibase_update"
