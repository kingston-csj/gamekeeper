import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import ServerList from '@/components/ServerList'
import UserSetting from '@/components/UserSetting'
import ServerMonitor from '@/components/ServerMonitor'
import ChannelPay from '@/components/ChannelPay'
import ChannelStatistics from '@/components/ChannelStatistics'

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
          path: '/serverMonitor',
          name: '服务监控',
          component: ServerMonitor,
          meta: {
            keepAlive: false
          }
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
