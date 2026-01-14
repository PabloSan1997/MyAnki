FROM sapmachine:21 AS BUILDER
WORKDIR /buildjdk
COPY . .
RUN ./mvnw clean package -DskipTests

FROM sapmachine:21-jre-alpine
WORKDIR /app
COPY --from=BUILDER /buildjdk/target/servicepart-1.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]