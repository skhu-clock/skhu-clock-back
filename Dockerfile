# jdk11 Image Start
FROM openjdk:11-jdk

# 인자 정리 - jar
ARG JAR_FILE=build/libs/*.jar

# jar File Copy
COPY ${JAR_FILE} app.jar

ARG SEVICE_KEY
ENV service=SEVICE_KEY

ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-DserviceKey=service", "-jar", "app.jar"]
