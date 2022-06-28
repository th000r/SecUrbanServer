#!/bin/bash

SQL_USER_FILE="../sql/user_setup.sql";
SQL_DB_FILE="../sql/db_setup.sql";

echo 'CREATE USER IF NOT EXISTS '$1'@"localhost" IDENTIFIED BY '"$2"';' > $SQL_USER_FILE;
echo 'GRANT ALL PRIVILEGES ON *. * TO '$1'@"localhost";' >> $SQL_USER_FILE;
echo 'FLUSH PRIVILEGES;' >> $SQL_USER_FILE;

echo 'CREATE DATABASE IF NOT EXISTS '$3';' > $SQL_DB_FILE;

echo 'CREATE TABLE IF NOT EXISTS '$3'.reports' >> $SQL_DB_FILE;
echo '(id INT UNSIGNED NOT NULL AUTO_INCREMENT,' >> $SQL_DB_FILE;
echo 'user_id VARCHAR(100) NOT NULL,' >> $SQL_DB_FILE;
echo 'message VARCHAR(255) NOT NULL,' >> $SQL_DB_FILE;
echo 'location BOOLEAN NOT NULL,' >> $SQL_DB_FILE;
echo 'latitude DOUBLE(16,4),' >> $SQL_DB_FILE;
echo 'longitude DOUBLE(16,4),' >> $SQL_DB_FILE;
echo 'picture BOOLEAN NOT NULL,' >> $SQL_DB_FILE;
echo 'source VARCHAR(255) NOT NULL,' >> $SQL_DB_FILE;
echo 'timestamp TIMESTAMP NOT NULL,' >> $SQL_DB_FILE;
echo 'PRIMARY KEY (id));' >> $SQL_DB_FILE;

echo 'CREATE TABLE IF NOT EXISTS '$3'.report_images(' >> $SQL_DB_FILE;
echo 'id INT UNSIGNED NOT NULL AUTO_INCREMENT,' >> $SQL_DB_FILE;
echo 'report_id INT UNSIGNED NOT NULL,' >> $SQL_DB_FILE;
echo 'image_name VARCHAR(30) NOT NULL,' >> $SQL_DB_FILE;
echo 'PRIMARY KEY(id),' >> $SQL_DB_FILE;
echo 'CONSTRAINT report_images FOREIGN KEY (report_id) references reports(id)' >> $SQL_DB_FILE;
echo ');' >> $SQL_DB_FILE;

