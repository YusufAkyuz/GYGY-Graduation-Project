# Build context repo kökü olmalıdır: docker build -f product-catalog-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY product-catalog-service/target/*.jar app.jar
EXPOSE 9003
ENTRYPOINT ["java", "-jar", "app.jar"]
