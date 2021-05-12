# Spring-Starter

This project is custom spring starter.

### Documentation API

```http request
{{host}}/api/documentation/swagger-ui/index.html
```

### Start software

```shell script
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev"
```

With spring-cloud-config

```shell script
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=dev -Dspring.cloud.bootstrap.enabled=true"
```
### Launch


[COMPLETE DOCUMENTATION](docs/table.md)