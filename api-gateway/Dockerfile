# Build context repo kökü olmalıdır: docker build -f api-gateway/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY api-gateway/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
