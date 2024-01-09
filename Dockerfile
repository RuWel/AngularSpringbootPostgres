FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="Rudi Welter"

ADD target/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
