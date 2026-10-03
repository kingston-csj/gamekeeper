gamekeeper是一个游戏后台管理平台，采用前后端分离架构。可供jforgame或mmorpg项目使用。
平台兼具**运维管控**与**游戏运营**两大核心能力：

- 统一运维底座：集中纳管全集群宿主机与游戏服务节点，提供实时服务器状态监控
- 日志检索能力：内置轻量化大数据看板「KFC 套餐」，可按需检索任意服务节点的运营日志、异常堆栈、业务常规日志
- 在线热更能力：支持服务代码、配置文件在线热更，以及游戏服务内存数据实时探查
- 一站式运营工作台：支持玩家、公会全维度数据与行为日志检索
- 线上运营操作：可执行玩家基础数据维护、全服邮件批量下发等运营操作，高效完成线上游戏业务管控


使用的前端技术：
vue + webpack + elementUi
使用的后端技术：
spring-boot全家桶

### 部分客户端运行效果
登录界面
![](/screenshots/login.png "登录界面")

主界面
![](/screenshots/main.png "主界面")

用户管理界面
![](/screenshots/user.png "用户管理界面 ")

角色管理界面
![](/screenshots/role.png "角色管理界面 ")

服务器列表界面
![](/screenshots/serverNodes.png "服务器列表界面 ")

服务器监控界面
![](/screenshots/serverStats.png "服务器监控界面 ")

玩家查询界面
![](/screenshots/playerInfo.png "玩家查询界面 ")

订单查询界面
![](/screenshots/payOrder.png "订单查询界面 ")

后台命令界面
![](/screenshots/adminCmd.png "后台命令界面 ")

机器总览界面
![](/screenshots/machineProfile.png "机器总览界面 ")

机器资源历史界面
![](/screenshots/machineHistory.png "机器资源历史界面 ")


### QuickStart
1. 安装git后，使用命令 git clone https://github.com/kingston-csj/gamekeeper
2. 客户端代码经过webpack打包后，放在admin/src/main/resources/static/路径下
3. 客户端源代码位于webapp下，如需二次开发，需安装npm环境
4. 客户端开发步骤，命令行环境cd webapp后，执行npm install下载依赖库
5. 客户端打包步骤，执行npm run build，把dist生成的static目录和index.html文件拷贝到服务端指定位置
6. 客户端本地开发，执行npm run start，开启代理服务器
7. 服务端创建admin数据库后，导入resouce/admin.sql。
8. 服务端启动入口为WebStartup.java  


## 欢迎交流讨论
欢迎star/fork，欢迎学习/使用，期待一起贡献代码 !!
如果您发现bug，或者有任何疑问，请提交issue !!