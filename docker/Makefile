.PHONY: all build release

IMAGE=glaizier/todo
VERSION=$(shell cat VERSION)

all: build

build:
	@cd ../; \
	  ./mvnw clean package -P default -Dspring.profiles.active=default; \
	  mkdir -p docker/app; \
	  cp target/todo.war docker/app;
	@docker build -t ${IMAGE} .

run: build
	@docker-compose up

release: build
	@docker build -t ${IMAGE}:${VERSION} .