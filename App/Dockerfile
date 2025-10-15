FROM maven:3.9-eclipse-temurin-22 AS builder
LABEL authors="manan"
WORKDIR /app
COPY pom.xml .
RUN mvn -U -B dependency:go-offline
COPY src ./src
RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:22-jdk

RUN apt-get update && apt-get install -y \
    openjfx \
    libgtk-3-0t64 \
    libgl1 \
    libglx0 \
    libgl1-mesa-dri \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
 && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/target/*.jar app.jar
ENV DISPLAY=host.docker.internal:0
CMD ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]

