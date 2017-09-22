# DC-Service-A

DC-Service-A是微服务架构demo中的Service-A应用角色。

dc-service-a集成了Eureka，Hystrix，Dc-Trace。在启动之前必须要有已运行的Eureka-server。

## 使用

### 青柳云安装
基于镜像快速部署请参考青柳云文档

环境变量配置

| 变量名 | 描述 |
| ---- | ----|
| MEMORY_LIMIT | 应用内存，e.g: 512 |
| WEB_CONTEXT | web应用访问的上下文，如果部署在应用访问规则是context方式的青柳云上，请务必与应用名保持一致。e.g: /myeureka-invoker |
| EUREKA_APPNAME | eureka服务名，与应用名保持一致 |
| EUREKA_HOME_URL | eureka注册-发现的地址，应是eureka-server的访问地址加上/eureka/ ，e.g:http://oss.mydomain.com:8041/myeureka-server/eureka/ |
| TURBINE_CLUSTER | 集群名称，如果当前应用不需要turbine集群配置，该变量请填写default |
| INVOKE_TRACE_PROJECT_CODE | 调用链项目编号,在项目下添加应用时，应用编号需要与当前镜像的EUREKA_APPNAME环境变量保持一致 |
| INVOKE_TRACE_HOME_URL | invoke-trace-server的地址，e.g: http://invoke-trace.my.com 或 http://192.168.1.199/invoke-trace-server ，不能以/结尾|


