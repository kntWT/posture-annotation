FROM openapitools/openapi-generator-cli

WORKDIR /local

COPY . .

RUN generate -i /local/openapi.yml -g typescript-axios -o /local/client
RUN generate -i /local/openapi.yml -g spring -o /local/api --additional-properties=Library=spring-boot

CMD ["bash"]