# DC-Zuul #
本应用是微服务DEMO中的API网关角色，主要负责接受外部请求并将请求转发给应用A（dc-service-a）。

DC-ZUUL集成了DC-Trace与Eureka组件，在对请求路由同时记录服务之间的调用关系。

## 使用

### 本地环境安装
在IDE中，运行本demo需要做如下调整

1. 修改`application.yaml`配置文件中的如下三项配置

* eureka-server地址：`eureka.serviceUrl.defaultZone`
* Dc-Trace地址：`platform.invokeTrace.url`
* 网关服务配置：`zuul.routes`

2. 打包。执行`mvn package -DskipTests`打包。
3. 运行。将打包好的.war文件复制到tomcat中，启动tomcat即可运行该demo。

zuul.routes配置参考http://cloud.spring.io/spring-cloud-netflix/multi/multi__router_and_filter_zuul.html

### 青柳云安装
基于镜像快速部署请参考青柳云文档(仅支持以二级域名区分应用的青柳云平台)

启动前需要预先启动dc-eureka-server，dc-service-a，dc-service-b等镜像，启动时需要设置以下几个环境变量

青柳云环境变量配置

| 变量名 | 描述 |
| ---- | ----|
| MEMORY_LIMIT | 应用内存，e.g: 512 |
| EUREKA_APPNAME | 服务名，与应用名保持一致 |
| EUREKA_HOME_URL | eureka注册-发现的地址，应是eureka-server的访问地址加上/eureka/ ，e.g:http://oss.mydomain.com:8041/myeureka-server/eureka/ |
| INVOKE_TRACE_PROJECT_CODE | 调用链项目编号,在项目下添加应用时，应用编号需要与当前镜像的EUREKA_APPNAME环境变量保持一致 |
| INVOKE_TRACE_HOME_URL | invoke-trace-server的地址，e.g: http://invoke-trace.my.com 或 http://192.168.1.199/invoke-trace-server ，不能以/结尾|
| INVOKE_TRACE_DASHBOARD_URL | invoke-trace-dashboard的访问地址 |
| SERVICE_A_APPNAME | service-a的应用名 |
| SERVICE_B_APPNAME | service-b的应用名 |
