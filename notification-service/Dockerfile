# Build context repo kökü olmalıdır: docker build -f notification-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY notification-service/target/*.jar app.jar
EXPOSE 9009
ENTRYPOINT ["java", "-jar", "app.jar"]
