FROM openjdk:17
ARG JAR_FILE=/target/*SNAPSHOT.jar
COPY ${JAR_FILE} driver.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","driver.jar"]