import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import Home from '@/components/Home'
import ServerList from '@/components/ServerList'
import UserMana from '@/components/UserMana'
import ServerMonitor from '@/components/ServerMonitor'
import ChannelPay from '@/components/ChannelPay'

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
      component: ChannelPay,
      name: '渠道管理',
      children: [
        {
          path: '/channel',
          iconCls: 'fa fa-user-o',
          name: '支付查询',
          component: ChannelPay
        }
      ]
    }, {
      path: '/home',
      component: Home,
      name: '用户管理',
      children: [
        {
          path: '/user',
          iconCls: 'fa fa-user-o',
          name: '用户管理',
          component: UserMana
        }
      ]
    }
  ]
})
