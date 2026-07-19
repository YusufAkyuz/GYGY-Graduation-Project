# Build context repo kökü olmalıdır: docker build -f billing-service/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY billing-service/target/*.jar app.jar
EXPOSE 9007
ENTRYPOINT ["java", "-jar", "app.jar"]
