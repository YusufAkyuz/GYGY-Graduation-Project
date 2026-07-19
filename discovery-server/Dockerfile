# Build context repo kökü olmalıdır: docker build -f discovery-server/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY discovery-server/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "app.jar"]
