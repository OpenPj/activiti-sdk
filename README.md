# Activiti SDK v1.0.0

The project consists of the following Maven submodules:

 * Activiti extensions JAR (`activiti-extensions-jar`): put here your Java extensions
 * Activiti App Overlay WAR (`activiti-app-overlay-war`): generate activiti-app WAR overlay with Activiti Extensions JAR embedded
 * Activiti Rest Overlay WAR (`activiti-app-rest-overlay-war`): generate activiti-rest WAR overlay with Activiti Extensions JAR embedded
 * Activiti Admin Overlay WAR (`activiti-admin-overlay-war`): generate activiti-admin WAR overlay
 * Activiti Overlay Docker (`activiti-app-overlay-docker`): put your overlayed WAR into the APS Docker container
 * Activiti Integration Tests (`activiti-integration-tests`): integration tests

## Capabilities
 * Full support of Arm64 CPUs (Apple Silicon M1) with native Docker containers
 * Running Docker will also create persistent volumes for each storage component (contentstore, db) for making the development approach in Activiti consistent and reliable

# Prerequisites
 * OpenJDK 11
 * Apache Maven 3.8.6
 * Docker (optional using `mvn clean install -DskipITs`)

# Quickstart

Full Maven lifecycle command:

 * `mvn clean install docker:build docker:start`
 
Endpoints available:

 * `http://localhost:8080/activiti-app` (default user: `admin/test`)
 * `http://localhost:8081/activiti-rest` (default user: `admin/test`)
 * `http://localhost:9090/activiti-admin` (default user: `admin/admin`)
 
Stop all the Docker containers with:
 
 * `mvn docker:stop`


# Activiti Extensions JAR Module

Folder structure is based on the same APS project classpath:
 * `/activiti-extensions-jar/src/test/resources/apps`: contains your own Activiti applications extracted
 * `/activiti-extensions-jar/src/test/java`: put here your unit and integration tests
 
To run use `mvn clean install -DskipITs`

 * Runs the embedded container + H2 DB 
 * Runs unit tests with `mvn clean test`
 * Creates JAR extensions
 
# SDK Packages - Runtime - Cross platform
 * `org.activiti.rest.service.api`: put here your enterprise custom REST endpoint (authentication required)
 
*Suggested naming conventions for implementations dedicated to a specific app*

 * `org.activiti.extension.*your_app*.listeners`: put here your listeners
 * `org.activiti.extension.*your_app*.service.tasks`: put here your service tasks

# SDK Packages - Embedded Test Runtime

 * `org.activiti.unit.tests`: put here your unit test (with suffix *Test.java)

# Activiti App WAR Overlay Module

This module is responsible for generating the final activiti-app.war artifact including all the extensions implemented in the Activiti extensions JAR Module.

# Activiti Rest WAR Overlay Module

This module is responsible for generating the final activiti-rest.war artifact including all the extensions implemented in the Activiti extensions JAR Module.

# Activiti Admin WAR Overlay Module

This module is responsible for generating the final activiti-admin.war artifact including the external configuration.

# Activiti Overlay Docker Module

For building the Docker container with your custom Activiti WARs:

`mvn docker:build`

Packaging of Activiti App Docker containers with extensions
 
`mvn docker:build docker:start`

Start your Activiti Docker containers with the following architecture:

  * PostgreSQL
  * activiti-db-volume: Docker volume for the database of Activiti Engine
  * activiti-admin-db-volume: Docker volume for the database of Activiti Admin
  * activiti-contentstore-volume: Docker volume for attachments

  * Stop your Docker container:

`mvn docker:stop`

  * Purge all the Docker volumes:
  
`mvn clean -Ppurge-volumes`

# Activiti Integration Tests Module
This module includes tests for interacting with the Activiti Platform on Docker using Http Client 5.

Put your Java test classes in the following package:
`/activiti-integration-tests/src/test/java`


# Building your Docker container (optional)
Update if you need the following configuration files:
 * _activiti-app-log4j.properties_  in `/activiti-docker-overlay/src/main/docker/logging`
 * _activiti-rest-log4j.properties_  in `/activiti-docker-overlay/src/main/docker/logging`
 * _activiti-app.properties_  in `/activiti-overlay-docker/src/main/docker/config`
 * _activiti-admin.properties_  in `/activiti-overlay-docker/src/main/docker/config`
 * _activiti-rest.properties_  in `/activiti-overlay-docker/src/main/docker/config/rest`
 * _engine.properties_  in `/activiti-overlay-docker/src/main/docker/config/rest`
 * _setenv.sh_  in `/activiti-overlay-docker/src/main/docker/config`
 
 All the Dockerfiles are available in `/activiti-overlay-docker/src/main/docker/config`:
 * _Dockerfile-app_  build the Activiti App WAR
 * _Dockerfile-rest_  build the Activiti Rest WAR
 * _Dockerfile-admin_  build the Activiti Admin WAR  
 

# Few things to notice

 * No parent pom
 * Standard JAR, WAR packaging with also Docker container generation
 * Works seamlessly with any IDE
 * Test your extensions with a consistent Activiti architecture running with Docker volumes

# Sponsorship
This project was created with the sponsorship of [Confcommercio Venezia CAF S.r.l](https://www.confcommercioveneziacaf.it).

# Contributors

# Enterprise support
Official maintenance and support of this project is delivered by Zia Consulting
