FROM openjdk:8-alpine
COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java", "-jar", "/usr/local/app/grep/lib/grep.jar"]
