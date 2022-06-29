#Get base image
FROM mariadb:10.8.2

#Arguments passed via cli
ARG db_root_pw
ARG db_pw
#ENV MARIADB_ROOT_PASSWORD=${db_root_pw}

#Set working directory and create folders
WORKDIR /home
RUN mkdir persuasion_app
WORKDIR /home/persuasion_app/
RUN mkdir sql
#RUN mkdir scripts

#Copy sql files
COPY /sql/*.sql /home/persuasion_app/sql/

#ADD ./scripts/init-db.sh ./scripts/init-db.sh
#RUN ./scripts/init-db.sh pw

#init the database
#scripts are executed in alphabetical order
#ADD /sql/01_user_setup.sql /docker-entrypoint-initdb.d
#ADD /sql/02_db_setup.sql /docker-entrypoint-initdb.d

