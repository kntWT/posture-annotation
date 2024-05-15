FROM eclipse-temurin

WORKDIR /app
RUN apt-get update && apt-get install -y maven entr
COPY . .
RUN mvn clean install

CMD ["mvn", "spring-boot:run", "-Dspring.devtools.restart.enabled=true", "-Dspring-boot.run.fork=false"]