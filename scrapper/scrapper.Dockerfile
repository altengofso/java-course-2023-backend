FROM openjdk:21
COPY target/scrapper.jar /app/scrapper.jar
WORKDIR /app
EXPOSE 8080
CMD ["java", "-jar", "scrapper.jar"]
