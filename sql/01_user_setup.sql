-- CREATE USER IF NOT EXISTS pa_root@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON persuasion_app. * TO pa_root@'localhost';
FLUSH PRIVILEGES;
