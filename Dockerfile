FROM openjdk:17-jdk-slim

# Cài đặt các gói cần thiết
RUN apt-get update && \
    apt-get install -y curl zip unzip

# Cài đặt SDKMAN
RUN curl -s "https://get.sdkman.io" | bash

# Chạy lệnh khởi động SDKMAN
RUN /bin/bash -c "source $HOME/.sdkman/bin/sdkman-init.sh"

# Cài đặt Gradle phiên bản 8.1.1
RUN /bin/bash -c "sdk install gradle 8.1.1"

WORKDIR /app

# Xây dựng ứng dụng bằng Gradle
RUN /bin/bash -c "gradle build"

# Sao chép tệp .jar của ứng dụng vào container Docker
COPY build/libs/*.jar /app/bot.jar

CMD ["java", "-jar", "/app/bot.jar"]