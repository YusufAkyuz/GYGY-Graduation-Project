# Build context repo kökü olmalıdır: docker build -f subscription-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY subscription-service/target/*.jar app.jar
EXPOSE 9005
ENTRYPOINT ["java", "-jar", "app.jar"]
