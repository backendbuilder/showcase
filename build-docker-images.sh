#!/bin/zsh

CURRENT_LOCATION=$(pwd)
# Copy the application JAR file into the container
#COPY $JAR_LOCATION app.jar
cd "${CURRENT_LOCATION}"/bank-service || exit
echo "gradle bootJar bank service starting"
gradle bootJar
cd "${CURRENT_LOCATION}"/api-gateway || exit
echo "gradle bootJar api gateway starting"
gradle bootJar
cd "${CURRENT_LOCATION}"/service-registry || exit
echo "gradle bootJar service-registry starting"
gradle bootJar

echo "Docker build starting"
docker build --tag=api-gateway:latest "${CURRENT_LOCATION}"/api-gateway
docker build --tag=service-registry:latest "${CURRENT_LOCATION}"/service-registry
docker build --tag=bank-service:latest "${CURRENT_LOCATION}"/bank-service