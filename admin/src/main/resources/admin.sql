/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : admin

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 07/06/2024 17:42:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '机构名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '上级机构ID，一级机构为0',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '机构管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (17, '完美集团', NULL, 0);
INSERT INTO `sys_dept` VALUES (18, 'efun集团', NULL, 1);
INSERT INTO `sys_dept` VALUES (19, '跃动集团555', NULL, 2);
INSERT INTO `sys_dept` VALUES (21, '上海分公司', 18, 0);
INSERT INTO `sys_dept` VALUES (22, '北京分公司', 17, 1);
INSERT INTO `sys_dept` VALUES (23, '广州分公司', 18, 1);
INSERT INTO `sys_dept` VALUES (25, '技术部', 22, 0);
INSERT INTO `sys_dept` VALUES (26, '技术部', 21, 0);
INSERT INTO `sys_dept` VALUES (27, '技术部', 23, 0);
INSERT INTO `sys_dept` VALUES (29, '市场部', 22, 0);
INSERT INTO `sys_dept` VALUES (30, '市场部', 23, 0);
INSERT INTO `sys_dept` VALUES (33, '魏国', 19, 0);
INSERT INTO `sys_dept` VALUES (34, '蜀国', 19, 1);
INSERT INTO `sys_dept` VALUES (35, '吴国', 19, 2);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据值',
  `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '标签名',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '类型',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '描述',
  `sort` decimal(10, 0) NOT NULL COMMENT '排序（升序）',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (3, 'male', '男', 'sex', '性别1', 0, '性别1');
INSERT INTO `sys_dict` VALUES (4, 'female', '女', 'sex', '性别', 1, '性别');

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单URL,类型：1.普通页面（如用户管理， /sys/user） 2.嵌套完整外部页面，以http(s)开头的链接 3.嵌套服务器页面，使用iframe:前缀+目标URL(如SQL监控， iframe:/druid/login.html, iframe:前缀会替换成服务器地址)',
  `perms` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：sys:user:add,sys:user:edit)',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) NULL DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 321 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, NULL, NULL, 0, 'el-icon-setting', 0);
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, '/sys/user', NULL, 1, 'el-icon-service', 1);
INSERT INTO `sys_menu` VALUES (3, '机构管理', 1, '/sys/dept', NULL, 1, 'el-icon-news', 2);
INSERT INTO `sys_menu` VALUES (4, '角色管理', 1, '/sys/role', NULL, 1, 'el-icon-view', 4);
INSERT INTO `sys_menu` VALUES (5, '菜单管理', 1, '/sys/menu', NULL, 1, 'el-icon-menu', 5);
INSERT INTO `sys_menu` VALUES (7, '字典管理', 1, '/sys/dict', NULL, 1, 'el-icon-edit-outline', 7);
INSERT INTO `sys_menu` VALUES (9, '查看', 2, NULL, 'sys:user:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, '新增', 2, NULL, 'sys:user:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, '修改', 2, NULL, 'sys:user:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, '删除', 2, NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (13, '查看', 3, NULL, 'sys:dept:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (14, '新增', 3, NULL, 'sys:dept:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (15, '修改', 3, NULL, 'sys:dept:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (16, '删除', 3, NULL, 'sys:dept:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, '查看', 4, NULL, 'sys:role:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, '新增', 4, NULL, 'sys:role:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, '修改', 4, NULL, 'sys:role:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, '删除', 4, NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, '查看', 5, NULL, 'sys:menu:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, '新增', 5, NULL, 'sys:menu:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, '修改', 5, NULL, 'sys:menu:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, '删除', 5, NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (28, '渠道管理', 0, NULL, NULL, 0, 'el-icon-picture-outline', 6);
INSERT INTO `sys_menu` VALUES (29, '订单查询', 28, '/pay/order', NULL, 1, 'el-icon-edit', 1);
INSERT INTO `sys_menu` VALUES (30, '渠道统计', 28, '/pay/statistics', NULL, 1, 'el-icon-picture', 2);
INSERT INTO `sys_menu` VALUES (31, '查看', 7, NULL, 'sys:dict:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (32, '新增', 7, NULL, 'sys:dict:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (33, '修改', 7, NULL, 'sys:dict:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (34, '删除', 7, NULL, 'sys:dict:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (100, '服务器管理', 0, '', '', 0, 'el-icon-info', 0);
INSERT INTO `sys_menu` VALUES (110, '服务列表', 100, '/server/nodes', '', 1, 'el-icon-view', 1);
INSERT INTO `sys_menu` VALUES (111, '服务列表-编辑', 110, '', 'server:nodes:edit', 2, 'el-icon-view', 1);
INSERT INTO `sys_menu` VALUES (112, '服务列表-新增', 110, '', 'server:nodes:add', 2, 'el-icon-view', 1);
INSERT INTO `sys_menu` VALUES (113, '服务列表-删除', 110, '', 'server:nodes:delete', 2, 'el-icon-view', 1);
INSERT INTO `sys_menu` VALUES (120, '后台命令', 100, '/server/commands', '', 1, 'el-icon-service', 2);
INSERT INTO `sys_menu` VALUES (200, '玩家管理', 0, '', '', 0, 'el-icon-service', 2);
INSERT INTO `sys_menu` VALUES (201, '玩家查询', 200, '/player/query', '', 1, 'el-icon-view', 0);
INSERT INTO `sys_menu` VALUES (300, '业务支持', 0, NULL, NULL, 0, 'el-icon-service', 0);
INSERT INTO `sys_menu` VALUES (310, '图片上传', 300, '/business/picture', NULL, 1, 'el-icon-service', 1);
INSERT INTO `sys_menu` VALUES (320, '字体上传', 300, '/business/font', NULL, 1, 'el-icon-service', 2);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '超级管理员');
INSERT INTO `sys_role` VALUES (2, 'dev', '开发人员');
INSERT INTO `sys_role` VALUES (3, 'test', '测试人员');
INSERT INTO `sys_role` VALUES (8, 'prod', '策划人员');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '机构ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色机构' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐',
  `email` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '机构ID',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户管理' ROW_FORMAT = DYNAMIC;

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
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 72 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2, 1);
INSERT INTO `sys_user_role` VALUES (26, 5, 3);
INSERT INTO `sys_user_role` VALUES (33, 6, 2);
INSERT INTO `sys_user_role` VALUES (34, 4, 2);
INSERT INTO `sys_user_role` VALUES (35, 9, 2);
INSERT INTO `sys_user_role` VALUES (36, 10, 3);
INSERT INTO `sys_user_role` VALUES (37, 11, 2);
INSERT INTO `sys_user_role` VALUES (38, 12, 3);
INSERT INTO `sys_user_role` VALUES (39, 15, 2);
INSERT INTO `sys_user_role` VALUES (41, 16, 3);
INSERT INTO `sys_user_role` VALUES (42, 8, 2);
INSERT INTO `sys_user_role` VALUES (43, 7, 4);
INSERT INTO `sys_user_role` VALUES (45, 18, 2);
INSERT INTO `sys_user_role` VALUES (46, 17, 3);
INSERT INTO `sys_user_role` VALUES (47, 3, 4);
INSERT INTO `sys_user_role` VALUES (48, 21, 2);
INSERT INTO `sys_user_role` VALUES (50, 23, 2);
INSERT INTO `sys_user_role` VALUES (51, 24, 3);
INSERT INTO `sys_user_role` VALUES (52, 25, 8);
INSERT INTO `sys_user_role` VALUES (53, 26, 2);
INSERT INTO `sys_user_role` VALUES (54, 27, 2);
INSERT INTO `sys_user_role` VALUES (56, 29, 8);
INSERT INTO `sys_user_role` VALUES (57, 31, 2);
INSERT INTO `sys_user_role` VALUES (58, 30, 2);
INSERT INTO `sys_user_role` VALUES (59, 32, 3);
INSERT INTO `sys_user_role` VALUES (68, 33, 2);
INSERT INTO `sys_user_role` VALUES (69, 22, 8);
INSERT INTO `sys_user_role` VALUES (70, 22, 2);
INSERT INTO `sys_user_role` VALUES (71, 28, 2);

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel`  (
  `channelNo` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '渠道码',
  `parentChannel` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父类渠道',
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

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
-- Table structure for t_font
-- ----------------------------
DROP TABLE IF EXISTS `t_font`;
CREATE TABLE `t_font`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `description` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_font
-- ----------------------------
INSERT INTO `t_font` VALUES ('665e8aa463261b357e2c5bad', 'c085d847f8394dbdbcf2e860d97085e9.ttf', 'AlibabaPuHuiTi.ttf', 'AlibabaPuHuiTi.ttf');

-- ----------------------------
-- Table structure for t_payorder
-- ----------------------------
DROP TABLE IF EXISTS `t_payorder`;
CREATE TABLE `t_payorder`  (
  `tradeNo` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易号',
  `oderId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `account` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `createTime` datetime NULL DEFAULT NULL COMMENT '充值时间',
  `money` int(11) NULL DEFAULT NULL COMMENT '充值金额',
  `gold` int(11) NULL DEFAULT NULL COMMENT '钻石',
  `orderId` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `server` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区组',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '订单状态',
  `channelCode` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '渠道',
  `roleUid` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`tradeNo`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_payorder
-- ----------------------------
INSERT INTO `t_payorder` VALUES ('1', '1', 'aaa', '2021-03-21 23:28:07', 121245, 45, '1', '飞狐外传', NULL, '2222DJ', NULL);
INSERT INTO `t_payorder` VALUES ('2', '2', 'bbb', '2021-03-21 23:28:07', 101000, 445, NULL, '飞狐外传', NULL, '6666DJ', NULL);
INSERT INTO `t_payorder` VALUES ('3', '3', 'ccc', '2021-03-21 23:28:07', 45, 45, NULL, '连城诀', NULL, 'DJ882', NULL);
INSERT INTO `t_payorder` VALUES ('4', '4', 'fff', '2021-03-21 23:28:07', 222222222, 345, NULL, '连城诀', NULL, 'DJ886', NULL);

-- ----------------------------
-- Table structure for t_picture
-- ----------------------------
DROP TABLE IF EXISTS `t_picture`;
CREATE TABLE `t_picture`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int(11) NULL DEFAULT NULL,
  `createTime` bigint(20) NULL DEFAULT NULL,
  `size` int(11) NULL DEFAULT 0,
  `width` int(11) NULL DEFAULT 0,
  `height` int(11) NULL DEFAULT 0,
  `url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_picture
-- ----------------------------
INSERT INTO `t_picture` VALUES ('665d9dcf95d0112bfd58ea2b', 0, 1717411279808, 66756, 672, 530, 'dc8c806029ae4d9aa7b4f71ca12e8305.jpeg', '1.jpeg');
INSERT INTO `t_picture` VALUES ('665d9dd395d0112bfd58ea2c', 0, 1717411283233, 13999, 600, 338, 'a111257cdb5a4e508366f3d42dfe0704.jpg', '1.jpg');
INSERT INTO `t_picture` VALUES ('665db7cb2f41b10ee203b6bd', 0, 1717417931169, 31705, 465, 309, '94d85c42ac3e4f759db838676145508e.png', '1.png');
INSERT INTO `t_picture` VALUES ('665db8882f41b10ee203b6be', 0, 1717418120366, 66756, 672, 530, '5a465a61c403491fac04cced48a8787f.jpeg', '1.jpeg');
INSERT INTO `t_picture` VALUES ('665db8a02f41b10ee203b6bf', 0, 1717418144033, 66756, 672, 530, 'e5747a4f1b294daa812287e63edc4898.jpeg', '1.jpeg');
INSERT INTO `t_picture` VALUES ('665ea6f1af082608bbe9a936', 0, 1717479153454, 2602, 254, 57, 'caf76061b953458d94bdfe017998ed97.png', '通道空白按钮1.png');
INSERT INTO `t_picture` VALUES ('665ea6f5af082608bbe9a937', 0, 1717479157661, 2382, 254, 57, '5eadc6ce4eb94f449965fa9085216cdd.png', '通道空白按钮2.png');

-- ----------------------------
-- Table structure for t_server
-- ----------------------------
DROP TABLE IF EXISTS `t_server`;
CREATE TABLE `t_server`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大区名称',
  `ip` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '大区ip',
  `port` int(11) NULL DEFAULT NULL,
  `httpPort` int(11) NULL DEFAULT NULL COMMENT '大区http端口',
  `merged` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23243 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_server
-- ----------------------------
INSERT INTO `t_server` VALUES (1, '内网开发1服', '127.0.0.1', NULL, 8080, 0);

SET FOREIGN_KEY_CHECKS = 1;
