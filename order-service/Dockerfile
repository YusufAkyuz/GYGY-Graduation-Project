# Build context repo kökü olmalıdır: docker build -f order-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY order-service/target/*.jar app.jar
EXPOSE 9004
ENTRYPOINT ["java", "-jar", "app.jar"]
