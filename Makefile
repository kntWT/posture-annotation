docker-up:
	docker compose up -d

docker-build:
	docker compose up -d --build

docker-restart:
	docker compose restart

docker-down:
	docker compose down	--rmi all

docker-down-v:
	docker compose down	--rmi all --volumes

openapi-generate-client:
	docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
		-i /local/openapi/openapi.yaml \
		-g typescript-axios \
		-o /local/client/src/lib/api/generated

openapi-generate-api:
	docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
		-i /local/openapi/openapi.yaml \
		-g spring \
		-o /local/api/generated \
		--additional-properties groupId=com.generated \
		--additional-properties artifactId=openapi-generated \
		--additional-properties apiPackage=com.generated.api \
		--additional-properties modelPackage=com.generated.model \
		--additional-properties=Library=spring-boot \
		--additional-properties interfaceOnly=true \
		--additional-properties java17=true \
		--additional-properties useSpringBoot3=true

recompile-api:
	docker compose exec api mvn compile