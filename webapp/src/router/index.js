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
import Cookies from "js-cookie"
import store from '@/store'
import {httpGet} from '../utils/httpclient'

Vue.use(Router)



const router = new Router({
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
      meta: {
        keepAlive: false,
        roles: ['ADMIN'] 
      },
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
          name: '后台指令',
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
      meta: {
        keepAlive: false,
        roles: ['ADMIN'] 
      },
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


router.beforeEach((to, from, next) => {
  // 登录界面登录成功之后，会把用户信息保存在会话
  // 存在时间为会话生命周期，页面关闭即失效。
  let token = Cookies.get('token')
  if (to.path === '/') {
    // 如果是访问登录界面，如果用户会话信息存在，代表已登录过，跳转到主页
    // if(token) {
    //   next({ path: '/home' })
    // } else {
      next()
    // }
  } else {
    if (!token) {
      // 如果访问非登录界面，且户会话信息不存在，代表未登录，则跳转到登录界面
      next({ path: '/' })
    } else {
      // 加载动态菜单和路由
      addDynamicMenuAndRoutes()
      next()
    }
  }
})

/**
* 加载动态菜单和路由
*/
function addDynamicMenuAndRoutes() {
  if(store.state.app.menuRouteLoaded) {
    console.log('动态菜单和路由已经存在.')
    return
  }

  findPemmison();

 
}

function findPemmison() {
   httpGet('/user/findPermissions', {
        }).then(resp=> {
          if (resp.status == 200) {
              store.commit('setPerms', resp.data.split(";"))

               for (var i = 0; i < router.options.routes.length; i++) {
                var r = router.options.routes[i];
                if (r.meta) {
                    if (!hasPermission(r.meta.roles)) {
                       r.hidden = true
                    }
                }
              }

              store.commit('menuRouteLoaded', true)

          }
        }, resp=> {

        });
}

function hasPermission (perms) {
  let has = store.state.user.perms
  if (!has) {
    return false
  }
  for(let i=0, len=perms.length; i<len; i++) {
      for(let j=0, len2=has.length; j<len2; j++) {
          if (perms[i] === has[j]) {
            return true
          }
       }
  }

  return false
}


export default router