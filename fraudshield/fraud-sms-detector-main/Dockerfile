FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw && ./mvnw clean package -DskipTests

EXPOSE 8080

ENV SPRING_DATA_MONGODB_URI=""
ENV FASTAPI_BASE_URL=""

CMD ["java", "-jar", "target/fraudshield-backend-1.0.0.jar"]