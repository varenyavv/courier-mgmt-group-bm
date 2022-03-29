## Prerequisites

1. Java Development Kit 11
2. Intellij IDEA (Community Edition)
3. Docker (Recommended. Otherwise, you will have to install Postgres server separately!)
4. Postgres server (If Docker is not used)

## Importing into Intellij

The `build.gradle` is configured with the Intellij Idea plugin. Run this command to generate the project files.

```
# Mac
./gradlew idea openIdea

# Windows
gradlew.bat idea openIdea
```

When Intellij opens, select `Import Gradle Project` and `Enable Auto-Import`.

## Building the project

```
# Mac
./gradlew build

# Windows
gradlew.bat build
```

## Spotless

[Spotless](https://github.com/diffplug/spotless) automatically keeps your code formatted. It is configured to use
the [Google Java Format](https://github.com/google/google-java-format).

There is an Intellij plugin called [google-java-format](https://plugins.jetbrains.com/plugin/8527-google-java-format/)
that you can install to have Intellij match the format too.

Running a build will automatically format the code.

## Running Locally

Postgres DB is added into the docker-compose file. Thus, running it locally using docker-compose is recommended.
Otherwise, Postgres DB server should be installed separately.

Most of the time, you will want to build the application into a Docker image and run it with docker compose. This
assumes you have previously run `./gradlew build`

```
#Start the application in detached mode using docker-compose
docker-compose up --build -d

#View the application logs
docker logs -f couriermgmt

#Stop and remove the locally running containers
docker-compose down
```

Alternatively, to start the application with gradle, you can run

```
# Mac or Windows Powershell
./gradlew run

# Windows
.\gradlew clean build

```

## Database Scripts

- This application is using Flyway for managing database script migration.
- The scripts are present under `src/main/resources/db/migration` folder.
- Strict naming convention should be followed `V$X__$Name.sql`. Where $X = next integer value.
- Before application starts, Flyway makes sure that all the scripts present in this directory should get executed on the
  connected database instance.

## API Documentation
- Once the application is up and running, use this URL to view the complete list of APIs implemented in this application:
  http://localhost:8080/api/swagger-ui/index.html
- We can also test the APIs using this link.