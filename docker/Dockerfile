#Get base image
FROM mariadb:latest

MAINTAINER "TU Darmstadt"

#Install git and clone project
RUN apt-get update \
     apt-get install -y git
RUN mkdir /home/app \
           cd /home/app \
           git clone https://github.com/th000r/SecUrbanServer.git \
           git checkout develop

#Set working directory
WORKDIR /home/app/SecUrbanServer

#create config file
ENV target_ip
ENV target_port
ENV db_user
ENV db_pw
ENV db_name

RUN chmod 755 ./scripts/make-config.sh
RUN ./scripts/make-config.sh ${target_ip} ${target_port} ${db_user} ${db_pw} ${db_name}

#create sql files
RUN chmod 755 ./scripts/make-sql.sh
RUN ./scripts/make-sql.sh ${db_user} ${db_pw} ${db_name}

#Create new mysql user
ENV db_root_pw
RUN mysql -uroot -p${db_root_pw} persuasion_app < ./sql/user_setup.sql

#Create database
ENV db_pa_pw
RUN mysql -upa -p${db_pa_pw} persuasion_app < ./sql/db_setup.sql

#remove sql and script files
RUN rm -rf sql
RUN rm -rf scripts

#Run server
RUN ./gradlew run
