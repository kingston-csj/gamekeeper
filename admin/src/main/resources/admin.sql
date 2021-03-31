/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50641
 Source Host           : localhost:3306
 Source Schema         : admin

 Target Server Type    : MySQL
 Target Server Version : 50641
 File Encoding         : 65001

*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) DEFAULT NULL COMMENT '机构名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级机构ID，一级机构为0',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='机构管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (17, '完美集团', NULL, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (18, 'efun集团', NULL, 1, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (19, '跃动集团', NULL, 2, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (21, '上海分公司', 18, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (22, '北京分公司', 17, 1, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (23, '广州分公司', 18, 1, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (25, '技术部', 22, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (26, '技术部', 21, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (27, '技术部', 23, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (29, '市场部', 22, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (30, '市场部', 23, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (33, '魏国', 19, 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (34, '蜀国', 19, 1, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_dept` VALUES (35, '吴国', 19, 2, 'admin', '2020-01-01 01:02:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` decimal(10,0) NOT NULL COMMENT '排序（升序）',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (3, 'male', '男', 'sex', '性别1', 0, 'admin', '2020-01-01 01:02:03', '性别1');
INSERT INTO `sys_dict` VALUES (4, 'female', '女', 'sex', '性别', 1, 'admin', '2020-01-01 01:02:03', '性别');
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, NULL, NULL, 0, 'el-icon-setting', 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, '/sys/user', NULL, 1, 'el-icon-service', 1, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (3, '机构管理', 1, '/sys/dept', NULL, 1, 'el-icon-news', 2, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (4, '角色管理', 1, '/sys/role', NULL, 1, 'el-icon-view', 4, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (5, '菜单管理', 1, '/sys/menu', NULL, 1, 'el-icon-menu', 5, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (6, '服务列表', 43, '/server/nodes', '', 1, 'el-icon-view', 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (7, '字典管理', 1, '/sys/dict', NULL, 1, 'el-icon-edit-outline', 7, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (9, '查看', 2, NULL, 'sys:user:view', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (10, '新增', 2, NULL, 'sys:user:add', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (11, '修改', 2, NULL, 'sys:user:edit', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (12, '删除', 2, NULL, 'sys:user:delete', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (13, '查看', 3, NULL, 'sys:dept:view', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (14, '新增', 3, NULL, 'sys:dept:add', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (15, '修改', 3, NULL, 'sys:dept:edit', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (16, '删除', 3, NULL, 'sys:dept:delete', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (17, '查看', 4, NULL, 'sys:role:view', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (18, '新增', 4, NULL, 'sys:role:add', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (19, '修改', 4, NULL, 'sys:role:edit', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (20, '删除', 4, NULL, 'sys:role:delete', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (21, '查看', 5, NULL, 'sys:menu:view', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (22, '新增', 5, NULL, 'sys:menu:add', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (23, '修改', 5, NULL, 'sys:menu:edit', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (24, '删除', 5, NULL, 'sys:menu:delete', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (28, '渠道管理', 0, NULL, NULL, 0, 'el-icon-picture-outline', 6, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (29, '订单查询', 28, '/pay/order', NULL, 1, 'el-icon-edit', 1, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (30, '渠道统计', 28, '/pay/statistics', NULL, 1, 'el-icon-picture', 2, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (31, '查看', 7, NULL, 'sys:dict:view', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (32, '新增', 7, NULL, 'sys:dict:add', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (33, '修改', 7, NULL, 'sys:dict:edit', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (34, '删除', 7, NULL, 'sys:dict:delete', 2, NULL, 0, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (38, '后台命令', 43, '/server/commands', NULL, 1, 'el-icon-warning', 1, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (41, '玩家查询', 44, '/player/query', '', 1, ' el-icon-view', 0, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (43, '服务器监控', 0, '', '', 0, 'el-icon-info', 1, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_menu` VALUES (44, '玩家管理', 0, '', '', 0, 'el-icon-service', 2, 'admin', '2020-01-01 01:02:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='角色管理';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理员', 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role` VALUES (2, 'dev', '开发人员', 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role` VALUES (3, 'test', '测试人员', 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role` VALUES (8, 'prod', '策划人员', 'admin', '2020-01-01 01:02:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '机构ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色机构';

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=806 DEFAULT CHARSET=utf8 COMMENT='角色菜单';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (224, 4, 1, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (225, 4, 2, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (226, 4, 9, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (227, 4, 3, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (228, 4, 13, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (229, 4, 4, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (230, 4, 17, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (231, 4, 5, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (232, 4, 21, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (233, 4, 6, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (234, 4, 7, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (235, 4, 31, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (236, 4, 8, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (237, 4, 25, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (238, 4, 26, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (239, 4, 27, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (240, 4, 28, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (241, 4, 29, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (242, 4, 30, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (243, 4, 35, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (783, 8, 9, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (784, 8, 13, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (785, 8, 17, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (786, 8, 21, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (787, 8, 31, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (788, 8, 6, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (789, 8, 35, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (794, 2, 1, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (795, 2, 43, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (796, 2, 44, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (797, 2, 41, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (798, 2, 35, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (799, 3, 1, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (800, 3, 2, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (801, 3, 9, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (802, 3, 3, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (803, 3, 13, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (804, 3, 43, NULL, '2020-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (805, 3, 6, NULL, '2020-01-01 01:02:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(40) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '机构ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COMMENT='用户管理';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', 'bd1718f058d8a02468134432b8656a86', 'YzcmCZNvbXocrsz9dm8e', 'hello@qq.com', '138XX', 1, 4, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (22, '全村的希望', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 34, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (23, '抢我辣条还敢跑', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 34, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (24, '身娇体软易推倒', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 34, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (25, '一懒众山小', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 33, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (29, '巡山小妖精', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 35, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (30, '萌可撩', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 35, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (31, '蓝莓格格', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 35, 'admin', '2020-01-01 01:02:03');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8 COMMENT='用户角色';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1, 1, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (2, 2, 1, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (26, 5, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (33, 6, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (34, 4, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (35, 9, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (36, 10, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (37, 11, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (38, 12, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (39, 15, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (41, 16, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (42, 8, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (43, 7, 4, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (45, 18, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (46, 17, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (47, 3, 4, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (48, 21, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (50, 23, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (51, 24, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (52, 25, 8, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (53, 26, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (54, 27, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (56, 29, 8, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (57, 31, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (58, 30, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (59, 32, 3, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (68, 33, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (69, 22, 8, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (70, 22, 2, NULL, NULL);
INSERT INTO `sys_user_role` VALUES (71, 28, 2, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_token
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_token`;
CREATE TABLE `sys_user_token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='用户Token';

-- ----------------------------
-- Records of sys_user_token
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_token` VALUES (1, 1, 'ee02ead2c1e3a113f82accafaf878b69', '2018-12-27 23:08:41', NULL, NULL);
INSERT INTO `sys_user_token` VALUES (2, 17, '3d32077ccddb6eb2c4302feb93765cd0', '2018-09-24 05:11:17', NULL, NULL);
INSERT INTO `sys_user_token` VALUES (3, 18, 'a939ac41fd309ca785485b4135b8baad', '2018-09-24 05:10:36', NULL, NULL);
INSERT INTO `sys_user_token` VALUES (4, 33, '605dbcfa2277cbca3b2a124974816080', '2018-11-04 21:42:49', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel` (
  `channelNo` varchar(128) NOT NULL COMMENT '渠道码',
  `parentChannel` varchar(128) DEFAULT NULL COMMENT '父类渠道',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`channelNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_channel
-- ----------------------------
BEGIN;
INSERT INTO `t_channel` VALUES ('2222DJ', NULL, '454');
INSERT INTO `t_channel` VALUES ('3333DJ', NULL, '45978');
INSERT INTO `t_channel` VALUES ('5555DJ', NULL, '23798');
INSERT INTO `t_channel` VALUES ('6666DJ', '2222DJ', '123123');
INSERT INTO `t_channel` VALUES ('8888DJ', '', '89');
INSERT INTO `t_channel` VALUES ('9999DJ', '', '56');
INSERT INTO `t_channel` VALUES ('DDOS', '', '5+64');
INSERT INTO `t_channel` VALUES ('DJ882', '8888DJ', '123');
INSERT INTO `t_channel` VALUES ('DJ883', '6666DJ', '132');
INSERT INTO `t_channel` VALUES ('DJ884', 'DJ883', '677');
INSERT INTO `t_channel` VALUES ('DJ885', '8888DJ', '222');
INSERT INTO `t_channel` VALUES ('DJ886', '8888DJ', '555');
COMMIT;

-- ----------------------------
-- Table structure for t_payorder
-- ----------------------------
DROP TABLE IF EXISTS `t_payorder`;
CREATE TABLE `t_payorder` (
  `tradeNo` varchar(128) NOT NULL COMMENT '交易号',
  `oderId` varchar(128) DEFAULT NULL COMMENT '订单号',
  `account` varchar(128) DEFAULT NULL COMMENT '账号',
  `createTime` datetime DEFAULT NULL COMMENT '充值时间',
  `money` int(32) DEFAULT NULL COMMENT '充值金额',
  `gold` int(32) DEFAULT NULL COMMENT '钻石',
  `orderId` varchar(128) DEFAULT NULL COMMENT '订单号',
  `server` varchar(128) DEFAULT NULL COMMENT '区组',
  `status` tinyint(8) DEFAULT NULL COMMENT '订单状态',
  `channelCode` varchar(128) DEFAULT NULL COMMENT '渠道',
  `roleUid` bigint(64) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`tradeNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_payorder
-- ----------------------------
BEGIN;
INSERT INTO `t_payorder` VALUES ('1', '1', 'aaa', '2021-03-21 23:28:07', 121245, 45, '1', '飞狐外传', NULL, '2222DJ', NULL);
INSERT INTO `t_payorder` VALUES ('2', '2', 'bbb', '2019-10-08 23:28:52', 101000, 445, NULL, '飞狐外传', NULL, '6666DJ', NULL);
INSERT INTO `t_payorder` VALUES ('3', '3', 'ccc', '2021-03-21 23:28:07', 45, 45, NULL, '连城诀', NULL, 'DJ882', NULL);
INSERT INTO `t_payorder` VALUES ('4', '4', 'fff', '2019-10-04 23:29:58', 222222222, 345, NULL, '连城诀', NULL, 'DJ886', NULL);
COMMIT;

-- ----------------------------
-- Table structure for t_server
-- ----------------------------
DROP TABLE IF EXISTS `t_server`;
CREATE TABLE `t_server` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL COMMENT '大区名称',
  `ip` varchar(128) DEFAULT NULL COMMENT '大区ip',
  `httpPort` int(32) DEFAULT NULL COMMENT '大区http端口',
  `jmxPort` int(11) DEFAULT NULL,
  `merged` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_server
-- ----------------------------
BEGIN;
INSERT INTO `t_server` VALUES (1, '飞狐外传', '127.0.0.1', 3307, 10081, 0);
INSERT INTO `t_server` VALUES (2, '雪山飞狐', '127.0.0.1', 3308, 10082, 0);
INSERT INTO `t_server` VALUES (3, '连城诀', '127.0.0.1', 3309, 10083, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
