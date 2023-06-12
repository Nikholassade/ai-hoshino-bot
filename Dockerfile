# Use an official OpenJDK image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Install Gradle
RUN wget https://services.gradle.org/distributions/gradle-8.1.1-bin.zip -P /tmp \
    && unzip -d /opt/gradle /tmp/gradle-*.zip \
    && ln -s /opt/gradle/gradle-8.1.1/bin/gradle /usr/bin/gradle \
    && rm /tmp/gradle-*.zip

# Build the project using Gradle
RUN gradle build

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the app when the container launches
CMD ["java", "-jar", "build/libs/app.jar"]