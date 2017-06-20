[![Build Status](https://travis-ci.org/GlaIZier/todo.svg?branch=master)](https://travis-ci.org/GlaIZier/todo)

## Todo
Spring mvc todo list app

### Tests
#### Embedded (HSQL) db implementation
```
mvn clean verify
```
#### Memory db implementation
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
or
```
https://localhost:8443/todo
```
and skip the warning because no trust certificate is used in the application

