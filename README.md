# Salt and Pepper

## A selfhosting recipe database

Features:
 * Adding recipes
 * Search recipes
 * Import from gourmet xml
 * Simple multiuser support

TODO:
 * Add view to only see your own recipes 
 * Refactor frontend
 * Add and document API
 * User management
 * Other auth providers
 * Other storage backends

## Building

```shell
./gradlew build
```

The build output is located under `build/libs/`

## Running

Use Java 11 to execute the `recipes-*-all.jar` from the build directory.

Specify the database backend with the environment variable `MICRONAUT_ENVIRONMENTS=h2` or
`MICRONAUT_ENVIRONMENT=postgres`.

Use `APPLICATION_USERS=file:/path/to/users.json` to specify the application users the server should setup upon startup.
If not specified the test users from `src/main/resources/users.json` are created.

Example: Running with h2
```shell
MICRONAUT_ENVIRONMENTS=h2 APPLICATION_USERS=file:/path/to/users.json java -jar ./recipes-0.1-all.jar
```

Example: Running with postgresql
```shell
MICRONAUT_ENVIRONMENTS=postgres APPLICATION_USERS=file:/path/to/users.json java -jar ./recipes-0.1-all.jar
```