SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for server_jvm_metrics
-- ----------------------------
DROP TABLE IF EXISTS `server_jvm_metrics`;
CREATE TABLE `server_jvm_metrics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `server_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '服务器实例id',
  `heap_used` float NOT NULL COMMENT '堆已使用 MB',
  `heap_committed` float NOT NULL COMMENT '已分配堆 MB',
  `heap_max` float NOT NULL COMMENT '最大堆 Xmx MB',
  `direct_used` float NOT NULL COMMENT '直接内存 MB',
  `process_cpu_percent` float NOT NULL COMMENT '进程CPU占用 %',
  `gc_cpu_percent` float NOT NULL COMMENT 'GC占用CPU %',
  `online_count` int(11) NOT NULL DEFAULT 0 COMMENT '在线人数',
  `collect_time` datetime NOT NULL COMMENT '采集时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_server_time`(`server_id`, `collect_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'JVM监控指标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of server_jvm_metrics
-- ----------------------------
INSERT INTO `server_jvm_metrics` VALUES (1, '1', 130.22, 256, 8176.08, 0.73, 12.55, 0.33, 125, '2026-08-23 00:00:00', '2026-08-23 00:00:02');
INSERT INTO `server_jvm_metrics` VALUES (2, '2', 96.31, 192, 4096, 0.56, 8.42, 0.22, 92, '2026-08-23 00:00:00', '2026-08-23 00:00:03');
INSERT INTO `server_jvm_metrics` VALUES (3, '1', 138.45, 256, 8176.08, 0.76, 13.28, 0.29, 138, '2026-08-23 01:00:00', '2026-08-23 01:00:02');
INSERT INTO `server_jvm_metrics` VALUES (4, '2', 103.52, 192, 4096, 0.59, 9.25, 0.26, 101, '2026-08-23 01:00:00', '2026-08-23 01:00:03');
INSERT INTO `server_jvm_metrics` VALUES (5, '1', 145.67, 288, 8176.08, 0.79, 14.96, 0.42, 152, '2026-08-23 02:00:00', '2026-08-23 02:00:02');
INSERT INTO `server_jvm_metrics` VALUES (6, '2', 110.73, 224, 4096, 0.61, 9.98, 0.31, 110, '2026-08-23 02:00:00', '2026-08-23 02:00:03');
INSERT INTO `server_jvm_metrics` VALUES (7, '1', 152.89, 288, 8176.08, 0.82, 15.53, 0.36, 165, '2026-08-23 03:00:00', '2026-08-23 03:00:02');
INSERT INTO `server_jvm_metrics` VALUES (8, '2', 117.19, 224, 4096, 0.63, 10.67, 0.28, 118, '2026-08-23 03:00:00', '2026-08-23 03:00:03');
INSERT INTO `server_jvm_metrics` VALUES (9, '1', 159.12, 320, 8176.08, 0.84, 16.89, 0.48, 178, '2026-08-23 04:00:00', '2026-08-23 04:00:02');

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
  `value` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据值',
  `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标签名',
  `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类型',
  `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `sort` decimal(10, 0) NULL DEFAULT NULL COMMENT '排序（升序）',
  `remarks` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 1102 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, NULL, NULL, 0, 'el-icon-setting', 0);
INSERT INTO `sys_menu` VALUES (2, '用户管理', 1, '/sys/user', NULL, 1, 'el-icon-user', 1);
INSERT INTO `sys_menu` VALUES (4, '角色管理', 1, '/sys/role', NULL, 1, 'el-icon-s-custom', 4);
INSERT INTO `sys_menu` VALUES (5, '菜单管理', 1, '/sys/menu', NULL, 1, 'el-icon-menu', 5);
INSERT INTO `sys_menu` VALUES (9, '查看', 2, NULL, 'sys:user:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (10, '新增', 2, NULL, 'sys:user:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (11, '修改', 2, NULL, 'sys:user:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (12, '删除', 2, NULL, 'sys:user:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (17, '查看', 4, NULL, 'sys:role:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (18, '新增', 4, NULL, 'sys:role:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (19, '修改', 4, NULL, 'sys:role:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (20, '删除', 4, NULL, 'sys:role:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (21, '查看', 5, NULL, 'sys:menu:view', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (22, '新增', 5, NULL, 'sys:menu:add', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (23, '修改', 5, NULL, 'sys:menu:edit', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (24, '删除', 5, NULL, 'sys:menu:delete', 2, NULL, 0);
INSERT INTO `sys_menu` VALUES (28, '运营统计', 0, NULL, NULL, 0, 'el-icon-s-data', 6);
INSERT INTO `sys_menu` VALUES (29, '订单查询', 28, '/pay/order', NULL, 1, 'el-icon-document', 1);
INSERT INTO `sys_menu` VALUES (30, '渠道统计', 28, '/pay/statistics', NULL, 1, 'el-icon-document', 2);
INSERT INTO `sys_menu` VALUES (100, '机器管理', 0, NULL, NULL, 0, 'el-icon-monitor', 0);
INSERT INTO `sys_menu` VALUES (101, '机器列表', 100, '/machine/nodes', NULL, 1, 'el-icon-menu', 1);
INSERT INTO `sys_menu` VALUES (102, '监控总览', 100, '/machine/profile', NULL, 1, 'el-icon-s-data', 2);
INSERT INTO `sys_menu` VALUES (103, '监控趋势图', 100, '/machine/detail', NULL, 1, 'el-icon-s-data', 2);
INSERT INTO `sys_menu` VALUES (200, '服务器管理', 0, '', '', 0, 'el-icon-monitor', 0);
INSERT INTO `sys_menu` VALUES (201, '服务列表', 200, '/server/nodes', '', 1, 'el-icon-menu', 1);
INSERT INTO `sys_menu` VALUES (202, '服务监控', 200, '/server/stats', '', 1, 'el-icon-s-data', 1);
INSERT INTO `sys_menu` VALUES (203, '运营命令', 200, '/server/gamecommands', '', 1, 'el-icon-s-tools', 2);
INSERT INTO `sys_menu` VALUES (204, '运维命令', 200, '/server/servercommands', NULL, 1, 'el-icon-s-tools', 3);
INSERT INTO `sys_menu` VALUES (205, '更新屏蔽词', 200, '/server/refreshdirtywords', NULL, 1, 'el-icon-edit', 3);
INSERT INTO `sys_menu` VALUES (211, '服务列表-编辑', 210, '', 'server:nodes:edit', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (212, '服务列表-新增', 210, '', 'server:nodes:add', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (213, '服务列表-删除', 210, '', 'server:nodes:delete', 2, NULL, 1);
INSERT INTO `sys_menu` VALUES (800, '游戏资料', 0, '', '', 0, 'el-icon-folder', 2);
INSERT INTO `sys_menu` VALUES (801, '玩家', 800, '/game/player', '', 1, 'el-icon-user-solid', 0);
INSERT INTO `sys_menu` VALUES (802, '公会', 800, '/game/guild', '', 1, 'el-icon-office-building', 0);
INSERT INTO `sys_menu` VALUES (900, 'sdk管理', 0, '', NULL, 0, 'el-icon-s-grid', 0);
INSERT INTO `sys_menu` VALUES (901, '游戏列表', 900, '/sdk/gamelist', NULL, 1, 'el-icon-folder', 1);
INSERT INTO `sys_menu` VALUES (902, '账号列表', 900, '/sdk/accountlist', NULL, 1, 'el-icon-user', 1);
INSERT INTO `sys_menu` VALUES (1000, '游戏日志', 0, '', NULL, 0, 'el-icon-notebook-2', 0);
INSERT INTO `sys_menu` VALUES (1001, '运营日志', 1000, '/gamelog/business', NULL, 1, 'el-icon-document', 1);
INSERT INTO `sys_menu` VALUES (1002, '异常日志', 1000, '/gamelog/exception', NULL, 1, 'el-icon-document', 1);
INSERT INTO `sys_menu` VALUES (1100, '运维操作', 0, '', NULL, 1, 'el-icon-s-tools', 0);
INSERT INTO `sys_menu` VALUES (1101, '数据库备份', 1100, '/yunwei/backup', NULL, 1, 'el-icon-s-tools', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色菜单' ROW_FORMAT = DYNAMIC;

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
-- Table structure for t_async_task
-- ----------------------------
DROP TABLE IF EXISTS `t_async_task`;
CREATE TABLE `t_async_task`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `task_id` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '对外唯一任务ID',
  `task_type` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '任务类型：DB_BACKUP / DB_CLEAN',
  `task_param` json NOT NULL COMMENT '任务入参，不同任务存各自参数',
  `status` varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT 'PENDING / PROCESSING / SUCCESS / FAIL',
  `progress` int(11) NULL DEFAULT 0 COMMENT '0~100，-1=失败',
  `url` varchar(512) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL COMMENT '备份任务：MinIO预签名下载链接；清库任务留空',
  `create_time` datetime NOT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_task_id`(`task_id`) USING BTREE,
  INDEX `idx_type_status`(`task_type`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_async_task
-- ----------------------------

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
INSERT INTO `t_channel` VALUES ('联运', '', 1);
INSERT INTO `t_channel` VALUES ('微信', '1', 2);
INSERT INTO `t_channel` VALUES ('快手', '1', 3);
INSERT INTO `t_channel` VALUES ('抖音', '1', 4);

-- ----------------------------
-- Table structure for t_machine
-- ----------------------------
DROP TABLE IF EXISTS `t_machine`;
CREATE TABLE `t_machine`  (
  `id` int(11) NOT NULL,
  `ip` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `agentUrl` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_machine
-- ----------------------------
INSERT INTO `t_machine` VALUES (1, '127.0.0.1', '11', 'http://127.0.0.1:8000');

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
INSERT INTO `t_payorder` VALUES ('1', '1', 'aaa', '2023-11-19 09:28:07', 121245, 45, '1', '飞狐外传', NULL, '2', NULL);
INSERT INTO `t_payorder` VALUES ('2', '2', 'bbb', '2023-11-19 09:28:07', 101000, 445, NULL, '飞狐外传', NULL, '3', NULL);
INSERT INTO `t_payorder` VALUES ('3', '3', 'ccc', '2023-11-19 09:28:07', 45, 45, NULL, '连城诀', NULL, '4', NULL);
INSERT INTO `t_payorder` VALUES ('4', '4', 'fff', '2023-11-19 09:28:07', 222222222, 345, NULL, '连城诀', NULL, '2', NULL);

-- ----------------------------
-- Table structure for t_sdkaccount
-- ----------------------------
DROP TABLE IF EXISTS `t_sdkaccount`;
CREATE TABLE `t_sdkaccount`  (
  `id` bigint(20) NOT NULL,
  `appId` varchar(64) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `account` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `pwd` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `authStatus` tinyint(4) NULL DEFAULT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `registerAt` bigint(20) NULL DEFAULT NULL,
  `lastLogin` bigint(20) NULL DEFAULT NULL,
  `idCard` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  INDEX `appId_account`(`appId`, `account`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sdkaccount
-- ----------------------------

-- ----------------------------
-- Table structure for t_sdkgame
-- ----------------------------
DROP TABLE IF EXISTS `t_sdkgame`;
CREATE TABLE `t_sdkgame`  (
  `appId` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `name` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `appKey` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `serverId` int(11) NULL DEFAULT NULL,
  `antiAddiction` tinyint(4) NULL DEFAULT NULL,
  PRIMARY KEY (`appId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sdkgame
-- ----------------------------
INSERT INTO `t_sdkgame` VALUES ('2587aa12', 'sdf', 'b1e1748b67484f6c990f0e84958c40ce', 0, 0);

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
  `useGate` int(11) NULL DEFAULT 0,
  `serverType` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_server
-- ----------------------------
INSERT INTO `t_server` VALUES (1, '兵临城下1服', '127.0.0.1', NULL, 8049, 0, 0, 'go');

SET FOREIGN_KEY_CHECKS = 1;
