[![Build Status](https://travis-ci.org/GlaIZier/todo.svg?branch=master)](https://travis-ci.org/GlaIZier/todo)

## Todo
Spring mvc todo list app

### Deploy
```
mvn clean tomcat7:redeploy -P local/docker
```

### Run embedded tomcat
```
mvn tomcat7:run
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