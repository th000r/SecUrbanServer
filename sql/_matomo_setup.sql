CREATE DATABASE IF NOT EXISTS matomo_securban;
CREATE USER IF NOT EXISTS 'matomo'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *. * TO matomo@'localhost';
FLUSH PRIVILEGES;
GRANT FILE ON *.* TO 'matomo'@'localhost';