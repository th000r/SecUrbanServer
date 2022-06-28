#!/bin/bash

CONFIG_FILE="../src/main/resources/config.json";

echo '{' > $CONFIG_FILE;
echo '"targetIp":"'$1'",' >> $CONFIG_FILE;
echo '"targetPort":'$2',' >> $CONFIG_FILE;
echo '"dbIp":"localhost",' >> $CONFIG_FILE;
echo '"dbPort":3306,' >> $CONFIG_FILE;
echo '"dbUserName":"'$3'",' >> $CONFIG_FILE;
echo '"dbPassword":"'$4'",' >> $CONFIG_FILE;
echo '"dbName":"'$5'",' >> $CONFIG_FILE;
echo '}' >> $CONFIG_FILE;


