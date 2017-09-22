#!/bin/bash
dockerize -template /root/tpl/application.yaml:/app_home/WEB-INF/classes/application.yaml \
          -template /root/tpl/server.xml:/opt/tomcat/conf/server.xml \
          /opt/tomcat/bin/catalina.sh run