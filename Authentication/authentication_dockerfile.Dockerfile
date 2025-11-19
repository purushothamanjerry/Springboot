# Multi-stage Dockerfile for building and running the Authentication Spring Boot app

# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy Maven wrapper (if present) and pom
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Ensure wrapper is executable
RUN if [ -f mvnw ]; then chmod +x mvnw; fi

# Download dependencies (offline) to speed up build
RUN if [ -f mvnw ]; then ./mvnw dependency:go-offline -B; else mvn dependency:go-offline -B; fi

# Copy sources and build the jar
COPY src ./src
RUN if [ -f mvnw ]; then ./mvnw -DskipTests package -B; else mvn -DskipTests package -B; fi

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy built jar from build stage (wildcard to accept the generated artifact name)
COPY --from=build /app/target/*.jar app.jar

# Use PORT env var provided by Render (fallback to 8080 locally)
ENV JAVA_OPTS=""
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar /app/app.jar"]
