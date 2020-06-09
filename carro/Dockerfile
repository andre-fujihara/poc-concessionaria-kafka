FROM openjdk:14.0

WORKDIR /app

VOLUME /tmp

COPY /target/*.jar /

ENTRYPOINT ["java", "-jar", "/carro-0.0.1-SNAPSHOT.jar"]