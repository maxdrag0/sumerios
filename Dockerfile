# Imagen base con Java 17
FROM eclipse-temurin:17-jdk

# Crea un directorio dentro del contenedor
WORKDIR /app

ARG JAR_FILE=target/sumerios-0.0.4.jar

# Copia el JAR compilado
COPY ${JAR_FILE} sumerios.jar

# Expone el puerto (mismo que usarás en docker-compose)
EXPOSE 8080

# Ejecuta la app
ENTRYPOINT ["java", "-jar", "sumerios.jar"]
