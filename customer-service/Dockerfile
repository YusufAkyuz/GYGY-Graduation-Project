# Build context repo kökü olmalıdır: docker build -f customer-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY customer-service/target/*.jar app.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "app.jar"]
