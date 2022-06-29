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
1. clone project\
```git clone https://github.com/th000r/SecUrbanServer.git```
2. build jar
   1. build fatJar with gradle shadow plugin\
   ```./gradlew shadowJar```
   2. change jar name under #run server in backend.Dockerfile to match jar in build/libs
3. build and run containers and set db root password and db user password\
```db_root_pw=pw db_pw=pw docker-compose up --force-recreate -d```
4. More commands
   1. open bash\
  ```docker exec -it mariadb_persuasion_app bash```
   2. stop container\
   ```docker stop [containerID]```
   3. remove container\
   ```docker rm [containerID]```
   3. remove images\
   ```docker rmi [containerID]```
   4. list all containers\
   ```docker container ls -a```
   5. show logs
   ```docker logs --tail 50 --follow --timestamps [containerID]```

## Configuration
- PORTS: ports can be edited in docker-compose.yaml


