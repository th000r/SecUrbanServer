#Get base image
FROM mariadb:latest

MAINTAINER "TU Darmstadt"

#Set working directory
WORKDIR /home
RUN mkdir persuasion_app
WORKDIR /home/persuasion_app/
RUN mkdir sql


#Create new mysql user
ENV db_root_pw
RUN mysql -uroot -p${db_root_pw} persuasion_app < ./sql/user_setup.sql

#Create database
ENV db_user
ENV db_pw

RUN mysql -u${db_user} -p${db_pw} persuasion_app < ./sql/db_setup.sql
