@cd ..
@call mvn -Pdev-JAR sql:execute liquibase:diff
