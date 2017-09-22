# MS-Client #

本应用是微服务DEMO中的Client角色，向Api-Gateway发起请求。

## 使用

### 青柳云安装
基于镜像快速部署请参考青柳云文档(仅支持以二级域名区分应用的青柳云平台)

青柳云环境变量配置

| 变量名 | 描述 |
| ---- | ----|
| MEMORY_LIMIT | 应用内存，e.g: 512 |
| APPNAME | 应用名 |
| ZUUL_URL | Dc-Zuul的地址(不包含访问协议），e.g: ms-zuul.eastdc.cn |
| INVOKE_TRACE_DASHBOARD_URL | invoke-trace-dashboard的访问地址 |
| SERVICE_B_APPNAME | demo中service-b的应用名 |
