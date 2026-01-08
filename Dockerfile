FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY . .

# Dar permisos de ejecución al wrapper de Maven
RUN chmod +x mvnw

# Build del proyecto
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/*.jar"]
