FROM eclipse-temurin:23-jdk

# 设置工作目录
WORKDIR /app

# 拷贝 JAR 到容器中
COPY out/artifacts/RabbitMQGetStarted_jar/RabbitMQGetStarted.jar /app/app.jar


# 启动主类所在的 jar
CMD ["java", "-jar", "/app/app.jar"]
