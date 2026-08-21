# Etapa 1: Compilarea aplicației folosind Maven și Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Rularea aplicației într-un mediu curat (doar JRE)
FROM eclipse-temurin:21-jre
WORKDIR /app
# Copiem fișierul .jar generat în etapa anterioară
COPY --from=build /app/target/*.jar app.jar
# Expunem portul pe care rulează Spring Boot
EXPOSE 8080
# Comanda de pornire
ENTRYPOINT ["java", "-jar", "app.jar"]