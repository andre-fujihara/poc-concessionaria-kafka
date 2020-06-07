FROM openjdk:14.0

WORKDIR /app

VOLUME /tmp

COPY /target/*.jar /

ENTRYPOINT ["java", "-jar", "/poc-concessionaria-carros-0.0.1-SNAPSHOT.jar"]