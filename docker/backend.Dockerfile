# get base image
FROM openjdk:11

WORKDIR /home
RUN mkdir persuasion_app_backend

# copy jar
COPY ./build/libs/*.jar /persuasion_app_backend/ktor-backend.jar
ENTRYPOINT ["java","-jar","/persuasion_app_backend/ktor-backend.jar"]