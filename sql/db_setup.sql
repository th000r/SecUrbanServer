-- CREATE DATABASE IF NOT EXISTS persuasion_app;

CREATE TABLE IF NOT EXISTS persuasion_app.reports
    (id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(100) NOT NULL,
    message VARCHAR(255) NOT NULL,
    location BOOLEAN NOT NULL,
    latitude DOUBLE(16,4),
    longitude DOUBLE(16,4),
    picture BOOLEAN NOT NULL,
    source VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS persuasion_app.report_images(
    id INT UNSIGNED NOT NULL AUTO_INCREMENT,
    report_id INT UNSIGNED NOT NULL,
    image_name VARCHAR(30) NOT NULL,
    PRIMARY KEY(id),
    CONSTRAINT report_images FOREIGN KEY (report_id) references reports(id)
);