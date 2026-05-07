# Usar imagen con Java 24
FROM maven:3.9.9-eclipse-temurin-24

# Configurar Maven con límites de memoria durante el build
ENV MAVEN_OPTS="-Xmx512m -Xms256m -XX:+UseG1GC"

WORKDIR /app

# Copiar pom.xml primero para cache de dependencias
COPY pom.xml .

# Descargar dependencias primero
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Build con límites de memoria más estrictos
RUN mvn clean package -DskipTests -Dmaven.compile.fork=true

EXPOSE 8080

# JVM optimizada para poca memoria en runtime
ENV JAVA_OPTS="-Xms256m -Xmx400m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

CMD ["sh", "-c", "java $JAVA_OPTS -jar target/*.jar"]