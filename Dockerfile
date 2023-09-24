FROM openjdk:17-alpine as builder

COPY . /app
WORKDIR /app
RUN ./gradlew clean build --info --stacktrace --no-daemon


FROM openjdk:17-alpine

COPY --from=builder /app/build/libs/challenge-0.0.1-SNAPSHOT.jar ./app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-server", "-jar", "/app/app.jar"]
