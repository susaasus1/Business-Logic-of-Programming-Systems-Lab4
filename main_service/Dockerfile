FROM openjdk:17-slim

ARG JAR_FILE=target/*.jar

COPY $JAR_FILE /main_service.jar

ENTRYPOINT exec java -jar /main_service.jar
