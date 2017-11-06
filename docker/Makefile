.PHONY: all build release

IMAGE=glaizier/todo
VERSION=$(shell cat VERSION)

all: build

build:
	@cd ../; \
	  ./mvnw clean package -P default -Dspring.profiles.active=default; \
	  mkdir -p docker/tomcat/7-jre8/app; \
	  cp target/todo.war docker/tomcat/7-jre8/app;
	@docker build -t ${IMAGE} ./tomcat/7-jre8/

run: build
	@docker run --rm -p 8080:8080 -p 8443:8443 --name todo ${IMAGE}

release: build
	@docker build -t ${IMAGE}:${VERSION} .