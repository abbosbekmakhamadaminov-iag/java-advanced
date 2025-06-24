# Dockerfile for Security Module Task 4
FROM openjdk:17-jdk-slim

LABEL maintainer="Security Homework Task 4"
LABEL description="Secret Providers Application with Spring Security"

# Set working directory
WORKDIR /app

# Copy the built JAR file
COPY build/libs/security-module-*.jar app.jar

# Expose port
EXPOSE 8084

# Set environment variables
ENV SPRING_PROFILES_ACTIVE=task4
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8084/task-info || exit 1

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]