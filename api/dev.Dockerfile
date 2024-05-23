FROM eclipse-temurin

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

# 自動生成された pom.xml から spring-boot-maven-plugin を削除
RUN sed -i -z -e '/<plugins>/,/<\/plugins>/ s|<plugin>\(\n\s*\)<groupId>org.springframework.boot<\/groupId>\(\n\s*\)<artifactId>spring-boot-maven-plugin</artifactId>\(\n\(.*\n\)*\s*\)<\/plugin>||g' generated/pom.xml

WORKDIR /app/parent
RUN mvn clean install

CMD ["mvn", "spring-boot:run", "-Dspring.devtools.restart.enabled=true", "-Dspring-boot.run.fork=false", "-Dspring.profiles.active=api"]
