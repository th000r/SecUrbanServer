# get base image
FROM matomo

WORKDIR /home
RUN mkdir persuasion_app_matomo
WORKDIR /home/persuasion_app_matomo
RUN mkdir scripts


# copy wait-for-it.sh to verfiy that database is running
COPY ./scripts/wait-for-it.sh /home/persuasion_app_matomo/scripts/wait-for-it.sh
RUN chmod 755 /home/persuasion_app_matomo/scripts/wait-for-it.sh