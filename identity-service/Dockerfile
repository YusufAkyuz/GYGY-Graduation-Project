# Build context repo kökü olmalıdır: docker build -f identity-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY identity-service/target/*.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]
