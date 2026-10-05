/* 
 * 接口统一集成模块
 */
import * as login from "./modules/login";
import * as user from "./modules/user";
import * as role from "./modules/role";
import * as menu from "./modules/menu";
import * as server from "./modules/server";
import * as machine from "./modules/machine";
import * as player from "./modules/player";
import * as pay from "./modules/pay";
import * as oss from "./modules/oss";
import * as sdk from "./modules/sdk";
import * as gamelog from "./modules/gamelog";
import * as yunwei from "./modules/yunwei";

// 默认全部导出
export default {
  login,
  user,
  role,
  menu,
  server,
  machine,
  player,
  pay,
  oss,
  sdk,
  gamelog,
  yunwei
};
