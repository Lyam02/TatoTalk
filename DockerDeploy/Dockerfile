FROM maven:3.9-eclipse-temurin-25 AS builder

WORKDIR /app

COPY ../pom.xml .
COPY ../src ./src

RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk25

ENV TZ=Europe/Paris
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN rm -rf /usr/local/tomcat/webapps/*
RUN mkdir "/fichierTatoTalk"

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]