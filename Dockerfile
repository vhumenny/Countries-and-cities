FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /usr/src/Countries-and-cities
COPY . .
RUN mvn clean install -Dmaven.test.skip

#app
FROM openjdk:17-alpine3.14
WORKDIR /app
COPY --from=builder /usr/src/Countries-and-cities/target/Countries-and-cities*.jar /app/Countries-and-cities.jar
CMD ["java", "-jar", "/app/Countries-and-cities.jar"]