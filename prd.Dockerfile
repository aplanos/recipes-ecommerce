FROM amazoncorretto:17
ARG JAR_FILE=target/recipes-api-0.0.1.jar
COPY ${JAR_FILE} app.jar
EXPOSE 80
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=prd","-Dspring-boot.excludeDevtools=true","app.jar"]
