# persuasion app server
The server component of a study at Technical University of Darmstadt.

# Setup (with Docker Container for MariaDB)

## Docker (MariaDB)

1. install docker
2. docker pull maria db image
docker pull mariadb:latest
  1. Verify image
  docker images
3. run container
docker run --name mariadb_persuasion_app -v /dir/to/data/mysql-data:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=[password] -p 6603:3306 -d mariadb
4. create new mysql user
  1. get container ip
  docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}' mariadb_persuasion_app
  2. connect to mysql
  mysql -h [IPAddress] -P 3306 -uroot -ppwd
  3. create new user
  CREATE USER IF NOT EXISTS pa_root@'%' IDENTIFIED BY [password];
  GRANT ALL PRIVILEGES ON *. * TO pa_root@'%';
  FLUSH PRIVILEGES;
  
5. More commands
  1. open bash
  docker exec -it mariadb_persuasion_app bash
  2. stop container
  docker stop [containerID]
  3. remove container
  docker rm [containerID]
  4. list all containers
  docker container ls -all

  
  
## Server
1. clone server 
git clone https://github.com/th000r/SecUrbanServer.git
2. change config under "resources/config.json"
3. run server
./gradlew run

## Create DB
1. load sql file
docker exec -i mariadb_persuasion_app  mysql -upa_root -p[password] persuasion_app < ./path/to/file.sql 






