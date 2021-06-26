CREATE USER username@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *. * TO username@'localhost';
FLUSH PRIVILEGES;
CREATE DATABASE database_name;
CREATE TABLE reports (report_id INT NOT NULL AUTO_INCREMENT, user_id VARCHAR(100) NOT NULL, message VARCHAR(255) NOT NULL, location BOOLEAN NOT NULL, latitude DOUBLE(16,4), longitude DOUBLE(16,4), picture BOOLEAN NOT NULL, source VARCHAR(255) NOT NULL, timestamp TIMESTAMP NOT NULL, PRIMARY KEY (report_id));