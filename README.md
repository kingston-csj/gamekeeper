gamekeeper是一个游戏后台管理平台，可供jforgame或mmorpg项目使用。  

使用的前端技术：
vue + webpack + elementUi
使用的后端技术：
spring-boot全家桶

### 部分客户端运行效果
登录界面  
![](/screenshots/login.png "登录界面")  

主界面  
![](/screenshots/main.png "主界面")  

订单查询界面 
![](/screenshots/payOrder.png "订单查询界面 ")  

后台命令界面 
![](/screenshots/adminCmd.png "后台命令界面 ")  


### QuickStart  
1. 安装git后，使用命令 git clone https://github.com/kingston-csj/gamekeeper
2. 客户端代码经过webpack打包后，放在admin/src/main/resources/static/路径下   
3. 客户端源代码位于webapp下，如需二次开发，需安装npm环境
4. 客户端开发步骤，命令行环境cd webapp后，执行npm install下载依赖库  
5. 客户端打包步骤，执行npm run build，把dist生成的static目录和index.html文件拷贝到服务端指定位置  
6. 服务端创建admin数据库后，导入resouce/admin.sql。  
7. 服务端启动入口为WebStartup.java  



## 欢迎交流讨论
欢迎star/fork，欢迎学习/使用，期待一起贡献代码 !!  
如果您发现bug，或者有任何疑问，请提交issue !!   