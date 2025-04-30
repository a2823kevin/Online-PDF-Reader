# === Step 1: Build Frontend ===
FROM node:20.17.0-bullseye AS frontend-build

WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend/ .
RUN npm run build

# === Step 2: Build Backend (with frontend) ===
FROM maven:3.9.9-amazoncorretto-17-debian-bookworm AS backend-build

WORKDIR /app
COPY backend/pom.xml backend/
COPY backend/src backend/src
RUN mkdir backend/assets
COPY --from=frontend-build /app/frontend/dist/frontend/browser/* backend/src/main/resources/static/

RUN mvn -f backend/pom.xml clean package -DskipTests

# === Step 3: Run Application ===
FROM openjdk:17-jdk-slim-buster

WORKDIR /app
COPY --from=backend-build /app/backend/target/*.jar server.jar
RUN mkdir /app/assets

EXPOSE 80
ENTRYPOINT ["java", "-jar", "server.jar"]
