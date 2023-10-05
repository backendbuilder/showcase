FROM gradle:jdk17 as builder

COPY --chown=gradle:gradle . /app

WORKDIR /app

RUN gradle bootJar

FROM amazoncorretto:17

# Run a shell command to find the location of the JAR file and store it in an environment variable
# RUN JAR_LOCATION=$(find ./build/libs -not -name "*plain.jar" -type f -exec echo {} \;)
# Copy the application JAR file into the container
#COPY $JAR_LOCATION app.jar

# Copy the application by hardcoded file name, TODO always build latest version, version msut be not hard coded
COPY --from=builder /app/build/libs/api-gateway-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
