# jdk11 Image Start
FROM openjdk:11-jdk

# 인자 정리 - jar
ARG JAR_FILE=/build/libs/skhuClock-0.0.1-SNAPSHOT.jar

# jar File Copy
COPY ${JAR_FILE} /app.jar


ENTRYPOINT ["java", ,"-jar","/app.jar"]
