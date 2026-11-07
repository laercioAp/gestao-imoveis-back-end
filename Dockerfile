
# ==========================================
# ETAPA 1 - COMPILACAO
# ==========================================
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN ./mvnw -B dependency:go-offline

COPY src/ src/

RUN ./mvnw -B clean package -DskipTests


# ==========================================
# ETAPA 2 - EXECUCAO
# ==========================================
FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 10000

ENTRYPOINT ["java", "-jar", "app.jar"]
