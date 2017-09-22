# DC-Eureka-Server #

本应用是微服务DEMO中的注册中心角色。主要负责服务的注册与订阅。


## 使用
### 青柳云安装
基于镜像快速部署请参考青柳云文档(仅支持以二级域名区分应用的青柳云平台)

启动时需要设置三个环境变量

| 变量名 | 描述 |
| ---- | ----|
| MEMORY_LIMIT | 应用内存，ex: 512  |
| WEB_CONTEXT | web应用访问的上下文，如果部署在应用访问规则是context方式的青柳云上，请务必与应用名保持一致。ex: /myeureka-server |
| EUREKA_HOME_URL | eureka注册-发现的地址，应是eureka-server的访问地址加上/eureka/ ，ex:http://oss.mydomain.com:8041/myeureka-server/eureka/ |

