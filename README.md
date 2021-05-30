# Aggregator

Aggregator of Ukraine IT companies

##Install Java 11

This sample requires you to have
[Java11](https://docs.oracle.com/en/java/javase/11/install/installation-jdk-microsoft-windows-platforms.html#GUID-A7E27B90-A28D-4237-9383-A58B416071CA)
.

##Install Gradle
This sample requires you to have
[Gradle](https://gradle.org/install/)
.

##Install Docker
On the next step install [Docker](https://docs.docker.com/get-docker/)
.

##Install Python
On the next step install [Python](https://docs.python.org/3/using/windows.html)
version 3.9.5.

Install psycopg2 for working with PostgreSQL
`pip install psycopg2`.

## Build portal
On the next step build portal by command bellow

`gradlew clean build`
.

## Build scanner
Then build scanner by command bellow
`gradlw clean build`
.

## Build and run PostgreSQL container
`docker compose up -d postgres`

## Init DB tables and data
Next step we need create db tables and create default admin account
`cd initDB`
`pytyhon inid.py`

## Start Firefox container before start scanner application
`cd ..`
`docker compose up -d firefox`

## Start HUB container before start scanner application
`docker compose up -d hub`

## Start scanner service
`docker comopose up -d scanner`

When scanner is finished first iteration, it should throw a message to log file 
'Founded companies is saved in PostgreSQL'.

## Start portal service
`docker compose up -d portal`

## Start client service
`docker compose up -d client`
