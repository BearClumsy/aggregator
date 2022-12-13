FROM amazoncorretto:11
RUN mkdir -p /user/src/app/
WORKDIR /user/src/app/
EXPOSE 8081

ADD build/libs/portal-0.0.2.jar /user/src/app/app.jar
ENTRYPOINT ["java","-jar","/user/src/app/app.jar"]
