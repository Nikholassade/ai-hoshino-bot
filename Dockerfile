# Sử dụng image base có chứa JDK 17
FROM openjdk:17-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép mã nguồn của ứng dụng vào Docker image
COPY . .

# Build ứng dụng bằng Gradle
RUN ./gradlew build

# Sao chép tệp JAR của ứng dụng vào image
COPY ./app/build/libs/app.jar .

# Thiết lập các tham số mặc định cho JVM
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Thiết lập lệnh mặc định khi chạy container
CMD ["java", "-jar", "app.jar"]