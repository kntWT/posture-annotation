FROM openjdk:17-jdk-slim as build-env

WORKDIR /app
RUN apt-get update && apt-get install -y maven
COPY . .
RUN mvn clean package

FROM debian:12-slim

WORKDIR /app
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjdk-17-jre-headless && \
    apt-get autoremove -y && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64/jre
ENV PATH=$PATH:$JAVA_HOME/bin

COPY --from=build-env /app/target/*.jar app.jar

EXPOSE 8000

ENTRYPOINT ["java","-jar","app.jar"]
