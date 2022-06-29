# get base image
FROM matomo

# copy wait-for-it.sh to verfiy that database is running
COPY ./scripts/wait-for-it.sh /home/persuasion_app_backend/scripts/wait-for-it.sh
RUN chmod 755 /home/persuasion_app_backend/scripts/wait-for-it.sh