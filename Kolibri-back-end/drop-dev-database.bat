set PGPASSWORD=postgres
psql postgresql://localhost:5432/kolibri_database_dev kolibri_user < drop-schema.sql
