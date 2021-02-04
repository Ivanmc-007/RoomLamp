FROM maven:3.6.3-openjdk-8 AS builder
COPY pom.xml /opt/app/
COPY src/ /opt/app/src
RUN mvn -f /opt/app/pom.xml clean package

FROM openjdk:8-alpine
COPY --from=builder /opt/app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar",  "app.jar"]