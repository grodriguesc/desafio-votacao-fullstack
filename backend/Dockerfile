FROM openjdk:17-slim

LABEL maintainer="votacao@example.com"
LABEL description="Sistema de Votação - Backend"

WORKDIR /app

# Copiar o JAR da aplicação
COPY target/votacao-backend-*.jar app.jar

# Expor a porta
EXPOSE 8080

# Variáveis de ambiente padrão
ENV JAVA_OPTS="-Xms256m -Xmx512m"
ENV SPRING_PROFILES_ACTIVE=prod

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
