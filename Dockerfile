FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . .

RUN ./gradlew build

CMD ["java", "-jar", "build/libs/Nachricht-0.0.1-SNAPSHOT.jar"]
