FROM openjdk:17
ARG JAR_FILE=/target/*SNAPSHOT.jar
COPY ${JAR_FILE} eureka.jar

EXPOSE 8761
ENTRYPOINT ["java","-jar","eureka.jar"]