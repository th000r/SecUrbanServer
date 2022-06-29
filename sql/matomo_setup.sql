CREATE DATABASE IF NOT EXISTS persuasion_app_matomo;
-- CREATE USER IF NOT EXISTS 'matomo_root'@'%' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON persuasion_app_matomo.* TO pa_root@'%';
FLUSH PRIVILEGES;
GRANT FILE ON persuasion_app_matomo.* TO 'pa_root'@'%';