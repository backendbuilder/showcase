# Use an official OpenJDK runtime as a parent image
FROM amazoncorretto:17-alpine3.18

# Run a shell command to find the location of the JAR file and store it in an environment variable
# RUN JAR_LOCATION=$(find ./build/libs -not -name "*plain.jar" -type f -exec echo {} \;)
# Copy the application JAR file into the container
#COPY $JAR_LOCATION app.jar

# Copy the application by hardcoded file name
COPY ./build/libs/service-registry-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your application will run on
EXPOSE 8081

# Define the default command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]