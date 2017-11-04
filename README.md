[![Build Status](https://travis-ci.org/GlaIZier/todo.svg?branch=master)](https://travis-ci.org/GlaIZier/todo)

# Todo
Spring mvc todo list app

## Tests
### Embedded (HSQL) and memory db implementation
```
mvn clean verify
```
### Production Postgres db tests
Remove @Ignore from ProdPersistenceTest and run tests again. Make sure that Postgres db is started and contains 
Role user and admin and doesn't contain any task or user

## Deploy and run
### Deploy to standalone tomcat
Tomcat is needed to be running on localhost:8080 and accept https on 8443. 
Also, maven settings in <home>/.m2/settings.xml must be consistent with <tomcat-home>/conf/tomcat-users.xml. 
Role "manager-gui" and user with that role must be set up in these files.

```
mvn clean tomcat7:redeploy -P <memory/default/prod> -Dspring.profiles.active=<memory/default/prod>
```

### Run embedded Maven plugin tomcat
#### Embedded db (HSQL) implementation
```
mvn tomcat7:run
```
#### Memory db implementation
```
mvn clean tomcat7:run -P memory -Dspring.profiles.active=memory
```

#### Postgres production db implementation
```
mvn clean tomcat7:run -P prod -Dspring.profiles.active=prod
```

### Docker
You need to install Docker locally to make all these steps work.
#### Postgresql docker
You can use docker postgres to make this application work with the production database and the production profile.
Pull image first
```bash
docker pull postgres:9.6.1
```
Run docker with relative volume mount, bridge ports, name postgres and remove container after finish from project root directory
```$bash
docker run --rm -p 5432:5432 -v $PWD/src/main/resources/sql/postgresql:/docker-entrypoint-initdb.d --name postgres postgres:9.6.1
```
Connect to the db to make sure that it works 
```bash
psql -U todoer -d tododb -h localhost
```

## Endpoints
### Application
```
localhost:8080/todo
```
or
```
https://localhost:8443/todo
```
Skip the warning because not trusted certificate is used in the application


###Rest API
```
https://localhost:8443/todo/api
```
or
```
https://localhost:8443/todo/swagger-ui.html
```
Swagger docs
```
http://localhost:8443/todo/v2/api-docs
```


## Additional info
If there are some problems that mvn couldn't find some resources try first
```bash
mvn clean compile
```
