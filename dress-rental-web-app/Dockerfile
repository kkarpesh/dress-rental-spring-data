FROM adoptopenjdk/openjdk11:alpine-jre
EXPOSE 8888
ARG JAR_FILE=build/libs/dress-rental-web-app.jar
COPY ${JAR_FILE} web-app.jar
ENTRYPOINT ["java","-jar","/web-app.jar", "--host=rest"]

