# persuasion app server
The server component of a study at Technical University of Darmstadt.

# Setup

## Docker
1. install docker
2. install docker-compose

### Info
- docker-compose.yaml builds and runs all containers
- if mariadb is created the first time and you want to run sql scripts, you have to mount them in docker-compose.yaml
   - sql scripts are not executed if folder "mariadb-data" contains files, run "/scripts/delete.db.sh" to delete them (make a backup!)

### Build and run
1. build and run containers and set db root password and db user password
```db_root_pw=pw db_pw=pw docker-compose up --force-recreate -d```
2. More commands
   1. open bash\
  ```docker exec -it mariadb_persuasion_app bash```
   2. stop container\
   ```docker stop [containerID]```
   3. remove container\
   ```docker rm [containerID]```
   4. list all containers\
   ```docker container ls -a```

## Configuration
- PORTS: ports can be edited in docker-compose.yaml

  
  
## Server
1. clone server\
```git clone https://github.com/th000r/SecUrbanServer.git```
2. change config under "resources/config.json"
3. run server\
```./gradlew run```

## Create DB
1. load sql file\
```docker exec -i mariadb_persuasion_app  mysql -upa_root -p[password] persuasion_app < ./sql/db_setup.sql```

## Create Folder Structure for images
1. create folder\
```mkdir upload/report```

## Run provided Dockerfile
1. build docker file\
```docker build -t persuasion_app_server .```
2. run docker container
```docker run --name persuasion_app_server -v /home/[user]/[pathtoserver]/mysql-data:/var/lib/mysql -v /home/[user]/[pathtoserver]/sql/user_setup.sql:/home/persuasion_app/sql/user_setup.sql -v /home/[user]/[pathtoserver]/sql/db_setup.sql:/home/persuasion_app/sql/db_setup.sql -e MYSQL_ROOT_PASSWORD=[root_password] -e db_root_pw=[root_password] -e db_user=[db_user] -e db_pw=[db_pw] -e db_name=persuasion_app -p 8888:8888 6603:3306 -d persuasion_app_server```

3. run docker-compose\
```sudo db_root_pw=pw db_pw=pw docker-compose up```



