# Readme
## Project "showcase": a simple bank app

Project "showcase" came to life to demonstrate some of my skills and simultaneously practice and learn some new skills.
The project became a simple bank app consisting of multiple microservices. 

The application allows users to create bank-accounts and send money to other bank-accounts.
The application allows users to be registered with either role "ADMIN" or role "CLIENT". With role ADMIN giving access to 
all the endpoints and role CLIENT having restricted access to endpoints. For example an ADMIN can create a bank-account 
for another user and set a custom balance amount, while a CLIENT can only create a bank-account for itself with a balance
of zero.



## The services
The two business logic is located inside the "bank-account-service" and the "transaction-service". 
They both produce to and consume messages from kafka topics.


### Steps to run the application
1. add a redirect for "keycloak.mydomain.nl" to 127.0.0.1 in etc/host file.
   1. open the file named "host" located in the /etc folder on your machine.
   2. add the following line and save the file, it will redirect requests to keycloak.mydomain.nl to 127.0.0.1, which is the same as localhost.
   ```bash
   127.0.0.1	keycloak.mydomain.nl
   ```
2. Make sure you have a running instance of Docker and Docker-compose. I'm using Docker Desktop which had both included.
   1. to check if you have Docker installed use the following command. I have Docker version 24.0.5 installed. 
   ```bash
     docker -v 
    ```
   2. to check if you have Docker-compose installed use the following command. I have Docker Compose version v2.20.2-desktop.1 installed.
   ```bash
   docker-compose -v
    ```
3. Now you can run all the services at once with a single Docker-compose command
```bash
    docker compose -f docker-compose.yml up --build 
```
1.the "--build" flag makes sure all the jar files are build before creating their images and running them in containers.

4. import Realm  (or will i fix auto import?)


super-user
tomjanssen - Password1@

1. I needed to give Keycloak a host-url to run on that is the same both inside the docker-network as on the host
   machine where I run the application on, otherwise the "issuer"-url of the security-token validation will see fail because
2. of having a different url that it will be validated against inside the dokcer network. Inside the docker-network KeyCloak will run on "keycloak.mydomain.nl"
2.