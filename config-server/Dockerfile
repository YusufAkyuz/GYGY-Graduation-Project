# Build context repo kökü olmalıdır: docker build -f config-server/Dockerfile .
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY config-server/target/*.jar app.jar
COPY config-repo /config-repo
ENV CONFIG_REPO_PATH=/config-repo
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "app.jar"]
