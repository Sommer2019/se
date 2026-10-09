# --- Build ---
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# --- Runtime ---
FROM eclipse-temurin:25-jre
WORKDIR /app
RUN useradd --system --uid 1001 app && mkdir data && chown app data
COPY --from=build /build/target/backend-*.jar app.jar
USER app
# Die H2-Datei liegt unter /app/data (siehe application.properties)
VOLUME /app/data
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
