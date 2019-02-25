FROM openjdk:11-jdk-slim as build
COPY . /app
WORKDIR /app
RUN ./gradlew --console=plain --no-daemon dependencies
RUN ./gradlew --console=plain --no-daemon build 

FROM gcr.io/distroless/java:11
COPY --from=build build/libs/homee_exporter.jar /homee_exporter.jar
CMD ["/homee_exporter.jar"]