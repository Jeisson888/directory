FROM amazoncorretto:21-alpine-jdk

COPY target/directory-0.0.1-SNAPSHOT.jar directory.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/directory.jar"]
