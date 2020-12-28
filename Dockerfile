FROM maven:3.6.3-openjdk-8

COPY . /opt/app/

WORKDIR /opt/app/

RUN mvn package

EXPOSE 8080

WORKDIR /opt/app/target/

CMD ["java", "-jar",  "app-room-lamp.jar"]
