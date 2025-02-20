# Build stage
FROM gradle:jdk21 AS build
WORKDIR /app
COPY . /app
RUN gradle build --no-daemon

# Package stage
FROM openjdk:21-jdk-slim
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY --from=build $APP_HOME/build/libs $APP_HOME
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "openai-bot.jar"]
