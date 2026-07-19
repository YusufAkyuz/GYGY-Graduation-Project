# Build context repo kökü olmalıdır: docker build -f usage-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY usage-service/target/*.jar app.jar
EXPOSE 9006
ENTRYPOINT ["java", "-jar", "app.jar"]
