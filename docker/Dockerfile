FROM openjdk:8-jre-alpine

COPY ./build/libs/spring-security.jar /app/app.jar

COPY ./docker/entrypoint.sh /app/entrypoint.sh

RUN apk add --no-cache tini

WORKDIR /app

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]
