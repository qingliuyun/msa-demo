# msa-demo

msa-demo（Microservice Architecture Demo）是青柳云推出的微服务框架demo。msa-demo主要演示了微服务模块之间调用关系与调用链路。

调用关系

客户端（dc-ms-client）-> API网关（dc-zuul）-> 服务提供者A（dc-service-a）-> 服务提供者B（dc-service-b） 

## msa-demo 主要包括如下工程

* dc-ms-client：服务消费者。主要通过API网关调用服务。

* dc-zuul： API网关。主要负责服务转发。

* dc-service-a：服务提供者A。

* dc-service-b：服务提供者B。

* dc-eureka-server：注册中心。dc-zuul、dc-service-a、dc-service-b 三个应用通过注册中心自动发现各自网络地址。

