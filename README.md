[![Build Status](https://travis-ci.org/GlaIZier/todo.svg?branch=master)](https://travis-ci.org/GlaIZier/todo)

# Todo
Spring mvc todo list app

## Tests
```
mvn clean verify -P <memory/default/prod>
```
Also some tests are marked with @Ignore. You can remove this annotation to make them run. For some cases external 
Postgres db is needed.

## Deploy and run
### Deploy to standalone tomcat
Tomcat is needed to be running on localhost:8080 and accept https on 8443. 
Also, maven settings in <home>/.m2/settings.xml must be consistent with <tomcat-home>/conf/tomcat-users.xml. 
Role "manager-gui" and user with that role must be set up in these files.

```
mvn clean tomcat7:redeploy -P <memory/default/prod>
```
In case of prod profile you need an external Postgres database

### Run Maven plugin's embedded tomcat
#### Embedded db (HSQL) implementation
```
mvn tomcat7:run
```
or 
```
mvn tomcat7:run -P default
```
#### Memory db implementation
```
mvn clean tomcat7:run -P memory
```

#### Postgres production db implementation
```
mvn clean tomcat7:run -P prod
```
In this case you need an external Postgres database.

### Run application in docker container
You need to install Docker locally to make this work, but you don't have to install the prod external database.
Go to docker folder and run
```
make run profile=<memory/default/prod>
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
### Knowing problems
#### Possible build problems
If there are some problems that mvn couldn't find some resources try first
```bash
mvn clean compile
```

#### Api access
If there is a problem with accessing API like 
```
net::ERR_INSECURE_RESPONSE
```
try to go to some application page first and accept that you understand that security certificate is untrusted. 
Todo uses a self-signed certificate. 

### Postgresql docker implementation to use with prod profile
You can use docker postgres to make this application work with the production database and the production profile (-P prod).
Install docker.
Pull the image:
```bash
docker pull postgres:9.6.1
```
Run docker with relative volume mount, bridge ports, name postgres and remove container after finish from project root directory:
```$bash
docker run --rm -p 5432:5432 -v $PWD/src/main/resources/sql/postgresql:/docker-entrypoint-initdb.d --name postgres postgres:9.6.1
```
Enter the container:
```
docker exec -it <postgres-container-id> bash
```
Connect to the db to make sure that it works 
```bash
psql -U todoer -d tododb -h localhost
```


### Profiles
There are 3 profiles in the application: memory, default and prod. They defer from each other by using different 
implementation of persistence. Memory uses Java's ConcurrentHashMaps to store users, roles and tasks; default - 
embedded HSQL db; prod - external PostgreSql instance. So, in the first two cases data is erased after restart, 
in the last - currently it is erased too when docker is used, but it's possible to make it constant. Also there is an
additional profile 'prod-docker'. It's a prod profile which is used only in Makefile with docker-compose because
tomcat docker container can't reach postgres on localhost. You don't need to use this profile separately without 
docker-compose in most of the cases.

These three profiles are connected with Spring profiles to allow to instantiate certain beans depending on different
profiles. All properties for these profiles are in the profiles folder under /resource. Maven filters these files, 
rename certain properties file connected to the profile to environment.properties. Maven plugins (tomcat7, maven-surefire)
adjust system property spring.profiles.active for themselves depending on used profile.

When spring application is starting on the external web server MvcWebAppInitializer onStartup() runs. This method tries 
to get the Spring active profile from system properties. If it fails it tries to read spring.profiles.active property 
from environment.properties file in classpath. After all these procedures the spring.profiles.active property is set up 
for the Spring application.

When tomcat7 plugin is used or maven tests are run MvcWebAppInitializer onStartup() doesn't execute. In these cases 
system property that was set up in pom file (for tomcat7, maven-surefire plugins) is used by Spring to determine which
Spring profile was set.