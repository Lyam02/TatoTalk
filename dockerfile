# Stage 1 : Build
FROM maven:3.9-eclipse-temurin-25 AS builder

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY src ./src

# Compiler l'application et créer le WAR
RUN mvn clean package -DskipTests

# Stage 2 : Runtime
FROM tomcat:10.1-jdk25

# Supprimer les applications par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier le WAR depuis le stage de build
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

# Exposer le port Tomcat
EXPOSE 8080

# Démarrer Tomcat
CMD ["catalina.sh", "run"]