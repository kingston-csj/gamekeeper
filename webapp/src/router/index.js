import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import ServerList from '@/components/ServerList'
import UserSetting from '@/components/UserSetting'
import ServerCommand from '@/components/ServerCommand'
import ChannelPay from '@/components/ChannelPay'
import ChannelStatistics from '@/components/ChannelStatistics'
import PlayerList from '@/components/PlayerList'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: '登录',
      hidden: true,
      component: Login
    }, {
      path: '/home',
      name: '',
      component: Home,
      hidden: true
    }, {
      path: '/home',
      component: Home,
      name: '服务器管理',
      iconCls: 'fa fa-file-text-o',
      children: [
        {
          path: '/serverList',
          name: '服务器列表',
          component: ServerList,
          meta: {
            keepAlive: true
          }
        }, {
          path: '/serverCommand',
          name: '代码热更',
          component: ServerCommand,
          meta: {
            keepAlive: false
          }
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '玩家管理',
      children: [
        {
          path: '/playerList',
          iconCls: 'fa fa-user-o',
          name: '玩家查询',
          component: PlayerList
        }
        ]
      }, {
      path: '/home',
      component: Home,
      name: '渠道管理',
      children: [
        {
          path: '/channelPay',
          iconCls: 'fa fa-user-o',
          name: '订单查询',
          component: ChannelPay
        },
        {
          path: '/channelStatistics',
          iconCls: 'fa fa-user-o',
          name: '渠道统计',
          component: ChannelStatistics
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '用户管理',
      children: [
        {
          path: '/userSetting',
          iconCls: 'fa fa-user-o',
          name: '修改密码',
          component: UserSetting
        }
      ]
    }
  ]
})
