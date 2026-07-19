# Build context repo kökü olmalıdır: docker build -f ticket-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY ticket-service/target/*.jar app.jar
EXPOSE 9010
ENTRYPOINT ["java", "-jar", "app.jar"]
