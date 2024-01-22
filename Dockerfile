FROM openjdk:11-jdk
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]