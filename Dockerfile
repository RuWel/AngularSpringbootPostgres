FROM eclipse-temurin:17-jdk-alpine
LABEL maintainer="Rudi Welter"

ARG git_repo=
ENV git_repo=$git_repo

RUN echo $git_repo

# INSTALL GIT
RUN apk add git

# CREATE WORKING DIR
WORKDIR /myapp

# CLONE GIT REPOSITORY
RUN git clone https://github.com/RuWel/AngularSpringbootPostgres.git .

# BUILD THE IMAGE
RUN ./mvnw clean install -DskipTests

# COPY JAR FROM TARGET
RUN ["sh", "-c", "cp target/*.jar app.jar"]

# REMOVER TARGET FOLDER
RUN ["rm", "-rf", "target"]

# EXECUTE JAR
ENTRYPOINT ["java","-jar","app.jar"]

