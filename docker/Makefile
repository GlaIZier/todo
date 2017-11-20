.PHONY: all build release

IMAGE=glaizier/todo
VERSION=$(shell cat VERSION)

all: build

build:
# ifeq ($(profile), $(filter $(profile), memory default)) - without presence check
# ((profile == memory || profile == default || profile == prod) && (profile is present))
ifneq (,$(filter $(profile),memory default))
	@cd ../; \
	  ./mvnw clean package -P $(profile); \
	  mkdir -p docker/tomcat/7-jre8/app; \
	  cp target/todo.war docker/tomcat/7-jre8/app;
	@docker build -t ${IMAGE} ./tomcat/7-jre8/
# for prod we skip tests because they require the db
else ifneq (,$(filter $(profile), prod))
	@cd ../; \
	  ./mvnw clean package -P $(profile)-docker -Dmaven.test.skip=true; \
	  mkdir -p docker/tomcat/7-jre8/app; \
	  cp target/todo.war docker/tomcat/7-jre8/app;
	@docker build -t ${IMAGE} ./tomcat/7-jre8/
else
	$(error Profile parameter should specified as memory, default or prod! Hint: profile=<memory/default/prod>)
endif

run: build
ifeq ($(profile), prod)
	@mkdir -p postgresql/init; \
	  cp ../src/main/resources/sql/postgresql/init.sql postgresql/init;
	@docker-compose up
else
	@docker run --rm -p 8080:8080 -p 8443:8443 --name todo ${IMAGE}
endif