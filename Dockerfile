FROM maven:3.8.4-openjdk-17-slim AS builder

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean install

FROM openjdk:17-oracle

WORKDIR /app

COPY --from=builder /app/target/url-shortener-1.0.0-SNAPSHOT.jar ./url-shortener.jar

#CMD ["java", "-jar", "url-shortener.jar"]
CMD ["java", "-jar", "url-shortener.jar", "com.urlshortener.UrlShortenerApplication"]
