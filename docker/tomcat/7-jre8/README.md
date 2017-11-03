# Tomcat docker container based on official one that has ability to maintain users' configs and applications 
## Directory structure
All directories are relative to Dockerfile placement.
* Place your keys for https in JKS keystore format in keys/
* Place your conf files to replace original ones in conf/
* Place your war applications for deployment in app/ or use docker mount ability to mount your apps directory to the container

### Build
From root directory
```
docker build -t glaizier/tomcat7jre8 .
```

### Run
Without mount
```
docker run --rm -p 8080:8080 -p 8443:8443 --name tomcat glaizier/tomcat7jre8
```
With mount
```
docker run --rm -p 8080:8080 -p 8443:8443 -v $PWD/app/:/usr/local/tomcat/webapps --name tomcat glaizier/tomcat7jre8
```