# seckill

## 系统介绍
本项目为基于Spring Boot开发的高性能简单秒杀系统，另外编写了纯Servlet版本，作为对比

## 开发环境
### 技术栈
框架：Spring Boot + MyBatis + Spring Boot Data Redis  
数据库：MySQL 8.0.20  
缓存：Redis 6.0  
限流：Guava RateLimiter

## 快速部署
### 编译
1.clone项目到本地  
2.打开seckill/pom.xml，注释  
```
<modules>
    <module>seckill-order</module>
    <module>seckill-web</module>
    <module>seckill-base</module>
</modules>
```
3.对seckill/pom.xml执行clean + install  
4.打开seckill/seckill-web/pom.xml，注释  
```
<modules>
    <module>web-servlet</module>
    <module>web-spring</module>
</modules>
```
5.对seckill/seckill-web/pom.xml执行clean + install  
6.取消seckill/seckill-web/pom.xml的注释、取消seckill/pom.xml的注释  
7.对seckill/pom.xml执行clean + install  

### 运行
1.新建数据库，执行./sql/db-init.sql(预生成10W条User信息)  
2.配置seckill-order项目数据库与redis  
3.执行seckill-order/src/test/java/Initialize.java
4.运行项目  

___注意___
- seckill-order先运行，web-servlet或web-spring后运行
### 部署
- seckill-order打包方式为jar，打包之后执行 java -jar xxx.jar即可  
- web-spring与web-servlet打包方式为war，打包之后复制到本地tomcat的webapps目录下  
## 文档
### 项目组成
* seckill
    * seckill-base ——项目公用类
    * seckill-order ——订单生成
    * seckill-web
        * web-servlet ——秒杀-Servlet版本
        * web-spring ——秒杀-Spring Boot版本
### API文档
___请求路径：___
- ` http://ip:port/[CONTEXT-PATH]/seckill `  

___请求方式：___
- GET

___参数：___  

|参数名|类型|是否必须|说明|
|:-----  |:-----|:-----|-----|
|userId|int|是|用户ID|
|productId|int|是|商品ID|

___返回示例：___
```
成功:
{
    "code": "0",
    "message": "秒杀成功",
    "data": null
}

失败:
{
    "code": "50001",
    "message": "产品已售光",
    "data": null
}
```
___错误码：___  

|code|说明|
|:-----  |:-----|
|0|秒杀成功|
|50001|产品已售光|
|40001|参数有误|
|40002|无权限秒杀该商品|
|40003|商品不存在|
|40004|用户不存在|
|40005|该商品已秒杀成功，无法再次秒杀|
|90000|系统繁忙|
|99999|系统异常|
### 秒杀流程
![process](https://github-img-bucket.oss-cn-beijing.aliyuncs.com/seckill/seckill-process.png)

## 压测
### 测试环境
- ___项目运行环境___   
OS: Windows10 x64  
CPU: i5-10210U 1.60GHz  
内存: 8G  
JDK: AdoptOpenJDK 11.0.8  
Tomcat: Tomcat 9.0(APR模式,-Xms2g -Xmx4g)  

- ___MySQL运行环境___  
百度云2C 4G计算型  
Docker+MySQL 8.0

- ___Redis运行环境___  
OS: CentOS 8.0  
CPU: i3-2375M 1.50GHz  
内存: 4G  
Docker+Redis 6.0

- ___压测环境___  
OS: Windows 10 x64  
CPU: i7-4710MQ 2.50GHz  
内存: 12G  
Jmeter  1000线程压测3分钟

- ___其他说明___  
百度云服务器网速只有1Mbps  
Redis所在的计算机、项目所在的计算机、压测计算机均为内网，使用网线连接到同一路由器。  
内网网速为千兆。

### 测试结果
- ___Spring Boot版___  
![test-boot](
https://github-img-bucket.oss-cn-beijing.aliyuncs.com/seckill/seckill-test-boot.png)  

- ___Servlet版___  
![test-servlet](https://github-img-bucket.oss-cn-beijing.aliyuncs.com/seckill/seckill-test-servlet.png)