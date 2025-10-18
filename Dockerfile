FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app
COPY . .

RUN mvn clean install dependency:copy-dependencies

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/target/bucket-s3-1.0-SNAPSHOT.jar app.jar
COPY --from=builder /app/target/dependency /app/lib

EXPOSE 8080

ENTRYPOINT ["java", "-cp", "app.jar:/app/lib/*", "com.sptech.school.Main"]
