FROM registry.qingliuyun.com:99/library/centos:7.0

# 安装jdk,unzip
RUN yum makecache fast && yum install -y java-1.8.0-openjdk && yum install -y wget && yum install -y unzip &&  yum install -y bc && yum clean all && mkdir /app_home
#install dockerize
RUN wget http://yum.dctech.club:81/release/infra/dockerize/dockerize-linux-amd64-v0.4.0.tar.gz && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-v0.4.0.tar.gz  && rm dockerize-linux-amd64-v0.4.0.tar.gz

# 设置locale
ENV LANG en_US.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL en_US.UTF-8
ENV TZ=Asia/Shanghai

# 安装tomcat
RUN mkdir /opt/tomcat
WORKDIR /opt/tomcat
RUN wget --cache=off  http://yum.dctech.club:81/release/infra/tomcat/tomcat-base-8.5.12.zip
RUN unzip tomcat-base-8.5.12.zip

# 设置jvm参数
COPY ./docker/setenv.sh /opt/tomcat/bin/
RUN chmod -R 775 /opt/tomcat/bin && rm -rf /opt/tomcat/conf/Catalina/localhost/ROOT.xml

WORKDIR /app_home

# 拷贝程序包
COPY eureka-server.war /app_home
RUN unzip -o -d /app_home  eureka-server.war && rm -rf eureka-server.war

# Dockerize 模版文件
COPY ./docker/tpl/application.yaml    /root/tpl/application.yaml
COPY ./docker/tpl/server.xml    /root/tpl/server.xml

COPY ./docker/start.sh    /app_home/bin/start.sh

RUN chmod 774 /app_home/bin/start.sh

# 暴露8080端口
EXPOSE 8080

# 启动容器执行的命令
CMD bash /app_home/bin/start.sh


