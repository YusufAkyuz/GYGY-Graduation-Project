# Build context repo kökü olmalıdır: docker build -f payment-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY payment-service/target/*.jar app.jar
EXPOSE 9008
ENTRYPOINT ["java", "-jar", "app.jar"]
