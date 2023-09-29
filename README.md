# Salt and Pepper
[![Build](https://github.com/detached/saltAndPepper/actions/workflows/build.yml/badge.svg)](https://github.com/detached/saltAndPepper/actions/workflows/build.yml)

## A selfhosting recipe database
## Building

```shell
./gradlew build
```

The build output is located under `build/libs/`

## Running

Use Java 17 to execute the `recipes-*-all.jar` from the build directory.

Specify the database backend with the environment variable `MICRONAUT_ENVIRONMENTS=h2` or
`MICRONAUT_ENVIRONMENT=postgres`.

Example: Running with h2
```shell
MICRONAUT_ENVIRONMENTS=h2 java -jar ./recipes-0.1-all.jar
```

Example: Running with postgresql
```shell
MICRONAUT_ENVIRONMENTS=postgres java -jar ./recipes-0.1-all.jar
```

Change the following settings in your application.yml:

```
application:
  initialAdminPassword: xyz
  
micronaut:
  security:
    token:
      jwt:
        signatures:
          secret:
            generator:
              secret: 'cGxlYXNlQ2hhbmdlVGhpc1NlY3JldEZvckFOZXdPbmU='
        generator:
          refresh-token:
            secret: 'cGxlYXNlQ2hhbmdlVGhpc1NlY3JldEZvckFOZXdPbmU='
```