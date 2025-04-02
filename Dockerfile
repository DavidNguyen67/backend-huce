FROM maven:3.9.9-amazoncorretto-21-al2023 AS base
WORKDIR /app
COPY . /app
RUN mvn install -DskipTests

FROM openjdk:21-rc-jdk-oracle
WORKDIR /app
COPY --from=base /app/target/backend-1.0.0.jar /app/backend-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend-1.0.0.jar"]
