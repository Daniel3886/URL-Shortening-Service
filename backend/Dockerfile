# Stage 1: Build
FROM gradle:8.14-jdk21 AS build

WORKDIR /app
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon || true
COPY src/ src/
RUN gradle bootJar --no-daemon

#TODO Figure out how to set the .env files properly for docker and flyway
# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]

EXPOSE 8080