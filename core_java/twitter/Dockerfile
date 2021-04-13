FROM openjdk:8-alpine
COPY target/twitter*.jar /usr/local/app/grep/lib/twitter.jar
ENTRYPOINT ["java", "-jar", "/usr/local/app/grep/lib/twitter.jar"]
