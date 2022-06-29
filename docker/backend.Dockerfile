# get base image
FROM openjdk:11

WORKDIR /home
RUN mkdir persuasion_app_backend
WORKDIR /home/persuasion_app_backend
RUN mkdir jar
RUN mkdir scripts

# copy wait-for-it.sh to verfiy that database is running
COPY ./scripts/wait-for-it.sh /home/persuasion_app_backend/scripts/wait-for-it.sh
RUN chmod 755 /home/persuasion_app_backend/scripts/wait-for-it.sh

# create upload folder
RUN mkdir upload
RUN mkdir report

# run server
COPY ./build/libs/SmartCityStudyServer-1.0-SNAPSHOT-all.jar /home/persuasion_app_backend/backend.jar
RUN chmod 755 /home/persuasion_app_backend/jar/backend.jar
ENTRYPOINT ["java","-jar","backend.jar"]