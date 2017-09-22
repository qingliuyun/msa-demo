#!/bin/bash
dockerize -template /root/tpl/application.yaml:/app_home/WEB-INF/classes/application.yaml \
          -template /root/tpl/server.xml:/opt/tomcat/conf/server.xml \
          -template /root/tpl/index.html:/app_home/WEB-INF/classes/static/index.html \
          /opt/tomcat/bin/catalina.sh run