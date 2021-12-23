#! /bin/sh

set -e

yarn --cwd ./src/main/js/ install --prefer-offline
yarn --cwd ./src/main/js/ run build
./gradlew build --info