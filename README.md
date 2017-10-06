[![Build Status](https://travis-ci.org/GlaIZier/todo.svg?branch=master)](https://travis-ci.org/GlaIZier/todo)

## Todo
Spring mvc todo list app

### Tests
#### Embedded (HSQL) and memory db implementation
```
mvn clean verify
```
#### Production Postgres db tests
Remove @Ignore from ProdPersistenceTest and run tests again. Make sure that Postgres db is started and contains 
Role user and admin and doesn't contain any task or user

### Deploy
```
mvn clean tomcat7:redeploy -P memory/default/prod
```

### Run embedded tomcat
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
and go to 
```
localhost:8080/todo
```
or (doesn't work for now)
```
https://localhost:8443/todo
```
and skip the warning because no trust certificate is used in the application

### Rest api
```
http://localhost:8080/todo/swagger-ui.html
```
### Postgresql docker
Pull image
```bash
docker pull postgres:9.6.1
```

Run docker with relative volume mount, bridge ports, name postgres and remove container after finish
```$bash
docker run --rm -p 5432:5432 -v $PWD/init:/docker-entrypoint-initdb.d --name postgres postgres:9.6.1
docker run --rm -p 5432:5432 -v $PWD/src/main/resources/sql/postgresql:/docker-entrypoint-initdb.d --name postgres postgres:9.6.1
```
Connect to db
```bash
psql -U todoer -d tododb -h localhost
```

### Additional info
If there are some problems that mvn couldn't find some resources try first
```bash
mvn clean compile
```