<template>
  <div class="page-container" style="width:99%;margin-top:15px;">
    <el-carousel :interval="3000" type="card" height="450px" class="carousel">
      <el-carousel-item class="carousel-item-intro">
        <h2>gamekeeper项目介绍</h2>
        <ul>
          <li>基于 Spring Boot、Vue、Element 搭建的管理平台</li>
          <li>旨在提供一套简洁易用的方案，帮助用户快速搭建游戏管理后台</li>
          <li>搭配一套成熟的游戏服务端框架，打造一站式服务架构</li>
          <li> 可供jforgame或mmorpg项目使用，包含权限控制</li>
        </ul>

      </el-carousel-item>
      <el-carousel-item class="carousel-item-func">
        <h2>功能计划</h2>
        <ul>
          <li>✔ 系统登录：系统用户登录，系统登录认证（token方式）</li>
          <li>✔ 用户管理：新建用户，修改用户，删除用户，查询用户</li>
          <li>✔ 机构管理：新建机构，修改机构，删除机构，查询机构</li>
          <li>✔ 角色管理：新建角色，修改角色，删除角色，查询角色</li>
          <li>✔ 菜单管理：新建菜单，修改菜单，删除菜单，查询菜单</li>
          <li>✔ 玩家信息管理：查询，封禁</li>
          <li>✔ 游戏服务器监控：在线人数，服务器指标</li>
          <li>✔ 订单查询：充值订单查询，充值渠道统计</li>
          <li>...未完待续</li>
        </ul>
      </el-carousel-item>
      <el-carousel-item class="carousel-item-env">
        <h2>开发环境</h2>
        <ul>
          <li>IDE : IDEA社区版本。     JDK : JDK 1.8.x。</li>
          <li>Maven : Maven 3.5.x。   MySQL: MySQL 5.7.x。</li>
          <li>IDE : SublimeText。     Webpack：webpack 3.2.x。</li>
          <li>NODE: Node 16.14.x。      NPM : NPM 8.3.x。</li>
        </ul>
        <h2>技术选型</h2>
        <ul>
          <li>后端框架：Spring Boot 2。  持久层框架：SpringJpa。 </li>
          <li>安全框架：spring-boot-starter-security </li>
          <li>前端框架：Vue.js。        页面组件：Element-UI。 </li>
          <li>...</li>
        </ul>
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  components: {},
  methods: {}
};
</script>

<style>
.carousel {
  padding-left: 20px;
  padding-right: 20px;
  margin-right: 20px;
}
.carousel h2 {
  color: #475669;
  font-size: 22px;
  opacity: 1.75;
  line-height: 100px;
  margin: 0;
}
.carousel ul {
  color: #475669;
  font-size: 15px;
  opacity: 1.75;
  line-height: 40px;
  margin: 0;
}
.carousel-item-intro h2 {
  color: #ffffff;
  font-size: 22px;
  opacity: 1.75;
  line-height: 80px;
  margin: 0;
}
.carousel-item-intro ul {
  color: #ffffff;
  font-size: 15px;
  opacity: 1.75;
  line-height: 65px;
  padding: 5px;
  margin: 0;
}
.carousel-item-func h2 {
  color: #3f393b;
  font-size: 22px;
  opacity: 1.75;
  line-height: 50px;
  margin: 0;
}
.carousel-item-func ul {
  color: #3f393b;
  font-size: 15px;
  opacity: 1.75;
  line-height: 30px;
  text-align: left;
  padding-left: 90px;
  margin: 0;
}
.carousel-item-env h2 {
  color: #475669;
  font-size: 22px;
  opacity: 1.75;
  line-height: 50px;
  margin: 0;
}
.carousel-item-env ul {
  color: #475669;
  font-size: 15px;
  opacity: 1.75;
  line-height: 35px;
  text-align: left;
  padding-left: 110px;
  margin: 0;
}
.carousel-item-intro {
  background-color: #19aaaf73;
  -webkit-border-radius: 25px;
  border-radius: 25px;
  -moz-border-radius: 15px;
  background-clip: padding-box;
  box-shadow: 0 0 25px #a3b3b965;
}
.carousel-item-func {
  background-color: #19aaaf73;
  -webkit-border-radius: 25px;
  border-radius: 25px;
  -moz-border-radius: 15px;
  background-clip: padding-box;
  box-shadow: 0 0 25px #a3b3b965;
}
.carousel-item-env {
  background-color: #19aaaf73;
  -webkit-border-radius: 25px;
  border-radius: 25px;
  -moz-border-radius: 15px;
  background-clip: padding-box;
  box-shadow: 0 0 25px #a3b3b965;
}
.carousel-item-intro {
  background-color: #b95e5e;
}
.carousel-item-func {
  background-color: #52c578;
}
.carousel-item-env {
  background-color: #41a7b9;
}
</style>
