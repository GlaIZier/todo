#!/bin/sh
docker rm $(docker ps -qa)
docker rmi $(docker images -a | grep '^<none>' | awk '{print $3}' ) 