[![Build Status](https://travis-ci.org/GlaIZier/todo.svg?branch=master)](https://travis-ci.org/GlaIZier/todo)

## Todo
Spring mvc todo list app

### Tests
#### Embedded (HSQL) db implementation
```
mvn clean verify
```
#### Memory db implementation
Doesn't work for now
```
mvn clean verify -DargLine="-Dspring.profiles.active=memory"
```

### Deploy
```
mvn clean tomcat7:redeploy -P local/docker
```

### Run embedded tomcat
#### Embedded db (HSQL) implementation
```
mvn tomcat7:run
```
#### Memory db implementation
```
mvn clean tomcat7:run -Dspring.profiles.active=memory
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
