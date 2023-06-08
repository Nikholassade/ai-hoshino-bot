FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar /app/bot.jar

CMD ["java", "-jar", "/app/bot.jar"]