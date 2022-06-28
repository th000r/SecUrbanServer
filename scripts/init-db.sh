#!/bin/bash

/usr/bin/mysqld_safe --skip-grant-tables &
sleep 5
mysql -u root -p $1 -e "CREATE DATABASE persuasion_app"
mysql -u root -p $1 persuasion_app < ./sql/user_setup.sql
mysql -u root -p $1 persuasion_app < ./sql/db_setup.sql