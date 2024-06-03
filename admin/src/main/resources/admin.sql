/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : localhost:3306
 Source Schema         : admin

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 13/04/2024 13:59:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '上级机构ID，一级机构为0',
  `order_num` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '机构管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
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

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据值',
  `label` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '标签名',
  `type` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '类型',
  `description` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '描述',
  `sort` decimal(10, 0) NOT NULL COMMENT '排序（升序）',
  `remarks` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (3, 'male', '男', 'sex', '性别1', 0, 'admin', '2020-01-01 01:02:03', '性别1');
INSERT INTO `sys_dict` VALUES (4, 'female', '女', 'sex', '性别', 1, 'admin', '2020-01-01 01:02:03', '性别');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `url` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)',
  `perms` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(0) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
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

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理员', 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role` VALUES (2, 'dev', '开发人员', 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role` VALUES (3, 'test', '测试人员', 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_role` VALUES (8, 'prod', '策划人员', 'admin', '2020-01-01 01:02:03');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '机构ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色机构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(0) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 841 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 1, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (2, 1, 2, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (3, 1, 3, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (4, 1, 4, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (5, 1, 5, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (6, 1, 6, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (7, 1, 7, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (8, 1, 8, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (9, 1, 9, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (10, 1, 10, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (11, 1, 11, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (12, 1, 12, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (13, 1, 13, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (14, 1, 14, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (15, 1, 15, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (16, 1, 16, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (17, 1, 17, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (18, 1, 18, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (19, 1, 19, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (20, 1, 20, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (21, 1, 21, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (22, 1, 22, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (23, 1, 23, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (24, 1, 24, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (25, 1, 25, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (26, 1, 26, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (27, 1, 27, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (28, 1, 28, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (29, 1, 29, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (30, 1, 30, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (31, 1, 31, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (32, 1, 32, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (33, 1, 33, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (34, 1, 34, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (35, 1, 35, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (36, 1, 36, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (37, 1, 37, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (38, 1, 38, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (39, 1, 39, 'admin', '2023-01-01 01:02:03');
INSERT INTO `sys_role_menu` VALUES (40, 1, 40, 'admin', '2023-01-01 01:02:03');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(0) NULL DEFAULT NULL COMMENT '机构ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', 'bd1718f058d8a02468134432b8656a86', 'YzcmCZNvbXocrsz9dm8e', 'hello@qq.com', '138XX', 1, 4, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (22, '全村的希望', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 34, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (23, '抢我辣条还敢跑', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 34, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (24, '身娇体软易推倒', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 34, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (25, '一懒众山小', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 33, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (29, '巡山小妖精', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 35, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (30, '萌可撩', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 35, 'admin', '2020-01-01 01:02:03');
INSERT INTO `sys_user` VALUES (31, '蓝莓格格', 'fd80ebd493a655608dc893a9f897d845', 'YzcmCZNvbXocrsz9dm8e', 'test@qq.com', '138XX', 1, 35, 'admin', '2020-01-01 01:02:03');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(0) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
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

DROP TABLE IF EXISTS `t_oss`;
CREATE TABLE `t_oss`  (
  `id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci ,
  `type` int  NULL DEFAULT NULL,
   `createTime` BIGINT NULL DEFAULT NULL ,
   `size` int  NULL DEFAULT 0 ,
  `width` int  NULL DEFAULT 0 ,
  `height` int  NULL DEFAULT 0,
  `url` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL ,
  `name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel`  (
  `channelNo` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '渠道码',
  `parentChannel` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '父类渠道',
  `id` bigint(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_channel
-- ----------------------------
INSERT INTO `t_channel` VALUES ('2222DJ', NULL, 22);
INSERT INTO `t_channel` VALUES ('3333DJ', NULL, 23);
INSERT INTO `t_channel` VALUES ('5555DJ', NULL, 24);
INSERT INTO `t_channel` VALUES ('6666DJ', '2222DJ', 25);
INSERT INTO `t_channel` VALUES ('8888DJ', '', 29);
INSERT INTO `t_channel` VALUES ('9999DJ', '', 30);
INSERT INTO `t_channel` VALUES ('DDOS', '', 31);
INSERT INTO `t_channel` VALUES ('DJ882', '8888DJ', 32);
INSERT INTO `t_channel` VALUES ('DJ883', '6666DJ', 33);
INSERT INTO `t_channel` VALUES ('DJ884', 'DJ883', 34);
INSERT INTO `t_channel` VALUES ('DJ885', '8888DJ', 35);

-- ----------------------------
-- Table structure for t_payorder
-- ----------------------------
DROP TABLE IF EXISTS `t_payorder`;
CREATE TABLE `t_payorder`  (
  `tradeNo` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '交易号',
  `oderId` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `account` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '账号',
  `createTime` datetime(0) NULL DEFAULT NULL COMMENT '充值时间',
  `money` int(0) NULL DEFAULT NULL COMMENT '充值金额',
  `gold` int(0) NULL DEFAULT NULL COMMENT '钻石',
  `orderId` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `server` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '区组',
  `status` tinyint(0) NULL DEFAULT NULL COMMENT '订单状态',
  `channelCode` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '渠道',
  `roleUid` bigint(0) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`tradeNo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_payorder
-- ----------------------------
INSERT INTO `t_payorder` VALUES ('1', '1', 'aaa', '2021-03-21 23:28:07', 121245, 45, '1', '飞狐外传', NULL, '2222DJ', NULL);
INSERT INTO `t_payorder` VALUES ('2', '2', 'bbb', '2021-03-21 23:28:07', 101000, 445, NULL, '飞狐外传', NULL, '6666DJ', NULL);
INSERT INTO `t_payorder` VALUES ('3', '3', 'ccc', '2021-03-21 23:28:07', 45, 45, NULL, '连城诀', NULL, 'DJ882', NULL);
INSERT INTO `t_payorder` VALUES ('4', '4', 'fff', '2021-03-21 23:28:07', 222222222, 345, NULL, '连城诀', NULL, 'DJ886', NULL);

-- ----------------------------
-- Table structure for t_server
-- ----------------------------
DROP TABLE IF EXISTS `t_server`;
CREATE TABLE `t_server`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '大区名称',
  `ip` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '大区ip',
  `httpPort` int(0) NULL DEFAULT NULL COMMENT '大区http端口',
  `jmxPort` int(0) NULL DEFAULT NULL,
  `merged` int(0) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_server
-- ----------------------------
INSERT INTO `t_server` VALUES (1, '飞狐外传', '127.0.0.1', 3307, 10081, 0);
INSERT INTO `t_server` VALUES (2, '雪山飞狐', '127.0.0.1', 3308, 10082, 0);
INSERT INTO `t_server` VALUES (3, '连城诀', '127.0.0.1', 3309, 10083, 0);

SET FOREIGN_KEY_CHECKS = 1;
