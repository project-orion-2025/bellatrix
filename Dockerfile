#FROM maven:3.8.5-openjdk-17-slim
#FROM maven:3.8.5-openjdk-17
FROM --platform=linux/amd64 maven:3.8.5-openjdk-17


WORKDIR /app
COPY . .

EXPOSE 8080

RUN mvn clean install -DskipTests

#CMD mvn spring-boot:run
CMD ["mvn", "spring-boot:run"]
