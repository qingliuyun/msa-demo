# DC-Service-A
DC-Service-A是微服务架构demo中的Service-B应用角色。

DC-Service-A集成了Eureka,在启动之前必须要有已运行的Eureka-server。

## 使用

### 青柳云安装
基于镜像快速部署请参考青柳云文档

环境变量配置
| 变量名 | 描述 |
| ---- | ----|
| MEMORY_LIMIT | 应用内存，ex: 512 |
| WEB_CONTEXT | web应用访问的上下文。如果青柳云是以上下文区分应用时，填写正斜杠加上应用名。如果是以二级域名区分应用，填写正斜杠。 |
| EUREKA_APPNAME | eureka服务名，与应用名保持一致 |
| EUREKA_HOME_URL | eureka注册-发现的地址，应是eureka-server的访问地址加上/eureka/ ，e.g:http://oss.mydomain.com:8041/myeureka-server/eureka/ |

启动后，访问eureka-server，查看服务是否注册成功。
