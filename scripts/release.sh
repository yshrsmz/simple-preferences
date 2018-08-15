#!/usr/bin/env bash

./gradlew clean
./gradlew build
./gradlew :annotation:bintrayUpload
./gradlew :processor:bintrayUpload
./gradlew :library:bintrayUpload
