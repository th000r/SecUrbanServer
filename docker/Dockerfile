#Get base image
FROM mariadb:latest

ARG db_root_pw
ENV MYSQL_ROOT_PASSWORD=${db_root_pw}

MAINTAINER "TU Darmstadt"

#Set working directory
WORKDIR /home
RUN mkdir persuasion_app
WORKDIR /home/persuasion_app/
RUN mkdir sql
COPY /sql/*.sql /home/persuasion_app/sql

#Create new mysql user
RUN mysql -uroot -p${db_root_pw} persuasion_app < ./sql/user_setup.sql

#Create database
ENV db_user user
ENV db_pw password

RUN mysql -u${db_user} -p${db_pw} persuasion_app < ./sql/db_setup.sql
