FROM eclipse-temurin

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

# maven pluginをskip
# RUN sed -i '/<\/build/i \
#         <plugins>\n\
#             <plugin>\n\
#                 <groupId>org.springframework.boot<\/groupId>\n\
#                 <artifactId>spring-boot-maven-plugin<\/artifactId>\n\
#                 <configuration>\n\
#                     <skip>true<\/skip>\n\
#                 <\/configuration>\n\
#             <\/plugin>\n\
#         <\/plugins>' generated/pom.xml

RUN sed -i '/<\/plugins>/i \
            <plugin>\n\
                <groupId>org.springframework.boot</groupId>\n\
                <artifactId>spring-boot-maven-plugin</artifactId>\n\
                <configuration>\n\
                    <skip>true</skip>\n\
                </configuration>\n\
            </plugin>' generated/pom.xml

WORKDIR /app/parent
RUN mvn clean install

CMD ["mvn", "spring-boot:run", "-Dspring.profiles.active=api"]
