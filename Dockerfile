# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

RUN apt-get update
RUN apt-get install -y dos2unix
RUN dos2unix gradlew

RUN bash gradlew fatJar
WORKDIR /run
RUN cp /app/build/libs/*.jar /run/hoshino.jar


# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the app when the container launches
CMD ["java", "-jar", "/run/hoshino.jar"]