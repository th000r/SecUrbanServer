CREATE USER IF NOT EXISTS username@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON *. * TO username@'localhost';
FLUSH PRIVILEGES;