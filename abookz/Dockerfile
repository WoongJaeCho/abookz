FROM amazoncorretto:17

COPY build/libs/abookz-0.0.1-SNAPSHOT.jar /app/abookz.jar
COPY entrypoint.sh /app/entrypoint.sh

ENV APP_HOME /app
EXPOSE 8088
VOLUME /app/upload

WORKDIR $APP_HOME

RUN chmod +x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]