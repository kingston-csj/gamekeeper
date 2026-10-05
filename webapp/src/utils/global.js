/**
 * 全局常量、方法封装模块
 * 通过原型挂载到Vue属性
 * 通过 this.Global 调用
 */

// 后台管理系统服务器地址
// 开发环境(dev)取 config/dev.env.js 的 BASE_URL，打包(build)取 config/prod.env.js 的 BASE_URL
export const baseUrl = process.env.BASE_URL;
export default {
  baseUrl
};
