/* 
 * 接口统一集成模块
 */
import * as login from './moudules/login'
import * as user from './moudules/user'
import * as dept from './moudules/dept'
import * as role from './moudules/role'
import * as menu from './moudules/menu'
import * as dict from './moudules/dict'
import * as log from './moudules/log'


import * as server from './moudules/server'
import * as player from './moudules/player'
import * as pay from './moudules/pay'

import * as oss from './moudules/oss'


// 默认全部导出
export default {
    login,
    user,
    dept,
    role,
    menu,
    dict,
    log,
    server,
    player,
    pay,
    oss
}