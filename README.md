# user_service
## Pulingle用户服务 
提供用户注册、登录、信息修改、好友系统等相关服务。

###  服务功能
* 账号管理：
  * 1.1	账号注册
用户通过注册流程进行初步信息录入，获得网站使用权限。用户首先填入手机（目前只支持手机号注册），前后端进行基本格式校验，符合格式要求后用户点击“发送验证码”后，后台会向用户填入的手机发送一则短信验证码（此处我们使用阿里的手机短信验证码服务）。后台使用Redis数据库记录对应验证码，以用户手机号拼接一个独特的字符串作为键值以方便后续匹配。用户手机收到短信验证码之后填写对应注册信息。最后填入用户名，以及用户密码（密码要求最少6位，前端作校验）。所有信息填写好后点击注册按钮进行注册。后台会首先校验用户填入的短信验证码是否匹配，若匹配则进行用户信息录入数据库，不匹配返回错误信息。此处数据库存入用户的密码，经过MD5加盐加密过。
  * 1.2	账号登录
用户通过登录流程进行网站的使用。用户填入注册过的账号（手机号）,注册时的密码，图形验证码（登录页面会显示4个图形验证码，此处验证码为后台生成一个base64编码字符串，存入Redis校验，失效为1分钟，用户可以点击刷新按钮获得新的图形验证码）。用户填入全部信息后点击登录按钮进行登录。后台首先检验验证码正确性，若正确进一步校验账号存在性，密码是否匹配。全部校验成功后，将根据用户信息生成一个带时效性的字符串令牌—Token，该Token为用户权限令牌，用于识别用户，将此值返回给客户端，客户端进行保留，之后的用户的操作将带上该令牌，后台用作权限验证。若校验码不正确则返回校验码错误信息。
  * 1.3	用户资料修改
用户可以进行基础信息的修改例如昵称，邮箱，个性签名，性别以及头像的修改。用户通过填入相应修改信息后点击保存按钮进行保存。后台验证修改后的信息合法性，若符合则更新相应数据库中的数据。其中修改头像用户先通过点击选择按钮进行图片选择后，进行图片的上传，用户确认后点击更新进行更新操作。
* 好友管理：
  * 2.1	用户搜索
用户进行用户的搜索。用户在搜索栏中填写用户的昵称，后台会根据用户输入的字符串进行模糊搜索，返回符合的用户信息列表（用户头像URL,昵称，用户ID）。用户可以点击列表用户的头像可跳转到该用户的主页。
  * 2.2	好友添加
用户在其他用户主页，若该用户不是登录用户的好友的话，则显示添加好友按钮，若没有则显示私信按钮。用户通过点击添加好友按钮则发送对应用户一则好友请求消息。对方用户在好友消息页面中显示好友请求消息，用户可以选择添加，或忽略
点击添加则双方互相添加成为好友，数据库中对双方数据记录进行更新（此处用户好友记录数据结构采用Redis的Set集合，一个用户对应一个Set），同时向双方发送一则添加好友成功的消息。
  * 2.3	好友删除
用户在好友列表点击好友删除按钮，确认后，后台更新双方Redis中的数据。
  * 2.4	好友信息列表
用户点击好友列表按钮，跳转到好友页面。页面显示该用户下全部好友，以及好友发送的最新一条消息。用户可以点击好友头像进行个人主页的跳转，或展开消息对话框进行私信发送。


## 关于Pulingle
### Pulingle简介
* Pulingle是我们小组三人（[zkTom](https://github.com/zkTom)、[TeemoSmithLee](https://github.com/TeemoSmithLee)）的本科毕业设计项目,是基于SpringCloud微服务架构的微社交应用。我们应用意在参考微信的朋友圈功能，设计一个功能更为简单的，界面更为简洁，包含动态发布、私信发送、图片分享等功能的社交软件。</br>
### 主要功能
1.	用户通过手机号，在进行短信验证码之后进行注册。
2.	用户注册之后可以凭注册账号密码进行登录，以进行应用提供功能服务。
3.	用户可以通过简单的操作进行文章，图片动态发布。
4.	用户可以浏览自己过往发布的动态消息，以及其评论消息。
5.	用户可以浏览朋友的动态，功能类比微信的朋友圈。并且可对其动态进行评论。
6.	用户可以上传照片到相册，并可以浏览。
7.	用户好友之间可以进行消息发送，并对用户新消息的推送。
8.	用户对账号资料的修改保存
###  项目架构

对功能的分析，以及结合微服务架构设计，把整个项目大致划分为4个服务。服务之间通过服务治理实现相互调用。
* 用户服务：提供用户注册、登录、信息修改、好友系统等相关服务。
* 动态服务：用户发布动态、浏览他人如好友或二次好友动态、评论他人动态等服务。
* 图片服务：提供用户上传图片、管理图片、相册图片管理等相对应服务。
* 消息服务：用户之间私信收发，系统消息发送，验证码，通知等服务。
![功能图](https://pulingle.oss-cn-shenzhen.aliyuncs.com/%E5%8A%9F%E8%83%BD%E5%9B%BE.png)

* 架构设计 
![Pulingle架构图](https://pulingle.oss-cn-shenzhen.aliyuncs.com/Pulingle%E6%9E%B6%E6%9E%84%E5%9B%BE%20%282%29.png)

组件说明：
Spring Cloud 组件：
* Eureka Server: 提供在分布式环境下的服务发现，服务注册的功能。每个服务及组件服务都在其中注册以及发现。
* 网关服务Zuul:边缘服务工具，是提供动态路由，监控，弹性，安全等的边缘服务。
* Config Server: 配置管理开发工具包，可以让你把配置放到远程服务器，目前支持本地存储、Git以及Subversion。这里我们采用从远程Github拉取配置信息。远程配置配置文件加密存储，从ConfigServer拉取时经过解密。
* Spring Cloud Bus: 事件、消息总线，用于在集群（例如，配置变化事件）中传播状态变化
* Git Repo:在Github上提供配置信息。
* RabbitMQ: 用于在分布式系统中存储转发消息，在易用性、扩展性、高可用性等方面都非常的优秀。是当前最主流的消息中间件之一。
#### 技术栈
* 后端</br>
•	Maven 3</br>
•	Java 8</br>
•	SpringCloud（Eureka、Zuul、Ribbon、Feign、Config）</br>
•	SpringBoot+MyBatis</br>
•	MySQL 5.7.22</br>
•	Redis 3.0.6</br>
* 前端</br>
•	node >= 8.9.3</br>
•	npm >= 5.5.1</br>
•	vue  </br>
•	axios  </br>
•	js-cookie  </br>
•	lodash </br>
•	vuex</br>
•	es6-promise</br>
•	photoswipe</br>
•	vue-lazyload </br>
* 使用RAP2进行接口文档管理: [RAP2](https://github.com/thx/RAP)
* 使用阿里云OSS服务作图片资源空间
* 短信验证码使用阿里云短信服务
#### 项目代码链接
* 微服务</br>
用户服务:   [user-service](https://github.com/Konoha-orz/user_service)</br>
消息服务:   [message-service](https://github.com/Konoha-orz/message_service)</br>
图片服务:   [picture-service](https://github.com/Konoha-orz/picture_service)</br>
动态服务:   [moment-service](https://github.com/Konoha-orz/moment_service)</br>
* Spring Cloud 组件</br>
服务发现：[eureka_server](https://github.com/Konoha-orz/eureka_server)</br>
服务网关：[gateway_zuul](https://github.com/Konoha-orz/gateway_zuul)</br>
服务配置：[config_server](https://github.com/Konoha-orz/config_server)</br>
* 统一管理配置</br>
[Pulingle-Config-Repo](https://github.com/Konoha-orz/Pulingle-Config-Repo)</br>
#### 项目部署
我们当初的项目是部署在阿里云ECS学生服务器上的。确保已安装环境依赖Java/MySQL/Redis</br>
* 部署运行顺序：</br>
1.eureka_server</br>
2.config_server和gateway_zuul</br>
3.user-service、message-service、picture-service、moment-service</br>
（注：确保根据jar运行环境在同一管理配置[Pulingle-Config-Repo](https://github.com/Konoha-orz/Pulingle-Config-Repo)配置好对应的信息，如端口、IP等）
###  项目演示</br>
* [视频演示地址](https://pulingle.oss-cn-shenzhen.aliyuncs.com/Pulingle%E6%BC%94%E7%A4%BA%E5%BD%95%E5%B1%8F.mp4)</br>
