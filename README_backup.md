# Readme
There are 2 options to run this application. You can run all the services in docker or you can just run keycloak and the databases in docker and all the other services directly with "java" 


### option 1: run everthing all at once with docker-compose:
to run all services in docker, with docker compose run the following command. The command also builds all the images
from the Docker-files located in each of the modules root folder, so it can be used after changes have been made to the
code.
```bash
    docker compose -f docker-compose_all.yml up --build 
```

### option 2: run keycloak and databases with docker-compose and the rest with java-command:
```bash
    docker compose -f docker-compose.yml up --build 
```

```bash
    gradlew bootJar <applicationClassName.java>
    java -jar ./api-gateway/build/libs/api-gateway-0\.0\.1-SNAPSHOT\.jar 
```

### Steps to run the application
1. add hosts to etc/host
2. running docker and docker-compose, with Docker Desktop you get both
3. run the following command to run all services
```bash
    docker compose -f docker-compose.yml up --build 
```
4. import Realm  (or will i fix auto import?)


super-user
tomjanssen - Password1@