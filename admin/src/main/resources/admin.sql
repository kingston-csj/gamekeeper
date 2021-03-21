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

 Date: 21/03/2021 12:56:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES (1, '超级管理员');
INSERT INTO `roles` VALUES (2, '普通用户');
INSERT INTO `roles` VALUES (3, '测试角色1');
INSERT INTO `roles` VALUES (4, '测试角色2');
COMMIT;

-- ----------------------------
-- Table structure for roles_user
-- ----------------------------
DROP TABLE IF EXISTS `roles_user`;
CREATE TABLE `roles_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rid` int(11) DEFAULT '2',
  `uid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `rid` (`rid`),
  KEY `roles_user_ibfk_2` (`uid`),
  CONSTRAINT `roles_user_ibfk_1` FOREIGN KEY (`rid`) REFERENCES `roles` (`id`),
  CONSTRAINT `roles_user_ibfk_2` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles_user
-- ----------------------------
BEGIN;
INSERT INTO `roles_user` VALUES (8, 2, 7);
INSERT INTO `roles_user` VALUES (9, 1, 7);
INSERT INTO `roles_user` VALUES (17, 3, 7);
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

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '1',
  `email` varchar(64) DEFAULT NULL,
  `userface` varchar(255) DEFAULT NULL,
  `regTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'vue', 'system', '123456', 1, 'hello@qq.com', ' ', '2017-12-08 09:30:22');
INSERT INTO `user` VALUES (3, 'springboot', '萌妹子', '123456', 1, 'world@qq.com', ' ', '2017-12-24 06:30:46');
INSERT INTO `user` VALUES (7, 'admin', '系统管理员', '123456', 1, 'java@qq.com', ' ', '2017-12-21 13:30:29');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
