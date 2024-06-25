FROM openjdk:21

EXPOSE 8181

ADD target/Cloud_server-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]