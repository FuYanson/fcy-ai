FROM node:20-alpine AS frontend
WORKDIR /app/frontend
COPY frontend/package.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

FROM maven:3.9-eclipse-temurin-17 AS backend
WORKDIR /app
COPY backend/pom.xml ./pom.xml
RUN mvn -B dependency:go-offline
COPY backend/src ./src
COPY --from=frontend /app/frontend/dist/ ./src/main/resources/static/
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=backend /app/target/clock-backend-0.0.1-SNAPSHOT.jar app.jar
ENV PORT=10000
EXPOSE 10000
CMD ["sh", "-c", "exec java -jar app.jar --server.port=${PORT}"]
