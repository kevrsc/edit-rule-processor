# Stage 1: Build application using Gradle 9 and Java 25
FROM gradle:9-jdk25 AS builder
WORKDIR /build
COPY --chown=gradle:gradle . .
RUN ./gradlew bootJar --no-daemon

# Stage 2: Minimal runtime image optimized for Java 25 Virtual Threads
FROM eclipse-temurin:25-jre-noble
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app --home /app app

COPY --from=builder /build/build/libs/*.jar app.jar
RUN chown app:app app.jar

USER app

ENV JAVA_OPTS="-XX:+UseZGC -XX:+UnlockExperimentalVMOptions -XX:+UseNUMA"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
