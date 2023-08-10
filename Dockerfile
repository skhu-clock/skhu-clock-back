# jdk11 Image Start
FROM openjdk:11-jdk

# 인자 정리 - jar
ARG JAR_FILE=build/libs/*.jar
ENV service $SERVICE_KEY
# jar File Copy
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=docker","-DserviceKey=service", "-jar", "app.jar"]
