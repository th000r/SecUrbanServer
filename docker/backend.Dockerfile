# get base image
FROM openjdk:11

WORKDIR /home
RUN mkdir persuasion_app_backend
WORKDIR /home/persuasion_app_backend
RUN mkdir jar
RUN mkdir scripts

COPY ./scripts /home/persuasion_app_backend/scripts

# run server
#ENTRYPOINT ["java","-jar","/jar/SmartCityStudyServer-1.0-SNAPSHOT-all.jar"]