FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8088
ARG JAR_FILE=build/libs/dress-rental-rest-app.jar
COPY ${JAR_FILE} rest-app.jar
ENTRYPOINT ["java","-jar","/rest-app.jar"]


