/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50561
Source Host           : localhost:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 50561
File Encoding         : 65001

Date: 2019-10-29 23:36:02
*/

SET FOREIGN_KEY_CHECKS=0;

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
INSERT INTO `roles` VALUES ('1', '超级管理员');
INSERT INTO `roles` VALUES ('2', '普通用户');
INSERT INTO `roles` VALUES ('3', '测试角色1');
INSERT INTO `roles` VALUES ('4', '测试角色2');

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
INSERT INTO `roles_user` VALUES ('8', '2', '7');
INSERT INTO `roles_user` VALUES ('9', '1', '7');
INSERT INTO `roles_user` VALUES ('17', '3', '7');

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel` (
  `channelNo` varchar(128) NOT NULL COMMENT '渠道码',
  `parentChannel` varchar(128) DEFAULT NULL COMMENT '父类渠道',
  `password` varchar(128) DEFAULT NULL COMMENT '密码',
  `auth` tinyint(1) DEFAULT NULL COMMENT '是否总代',
  `point` int(32) DEFAULT NULL COMMENT '剩余点数',
  PRIMARY KEY (`channelNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_channel
-- ----------------------------
INSERT INTO `t_channel` VALUES ('2222DJ', null, '454', '1', null);
INSERT INTO `t_channel` VALUES ('3333DJ', null, '45978', '1', null);
INSERT INTO `t_channel` VALUES ('5555DJ', null, '23798', '1', null);
INSERT INTO `t_channel` VALUES ('6666DJ', '2222DJ', '123123', '1', null);
INSERT INTO `t_channel` VALUES ('8888DJ', '', '89', '1', null);
INSERT INTO `t_channel` VALUES ('9999DJ', '', '56', '1', null);
INSERT INTO `t_channel` VALUES ('DDOS', '', '5+64', '1', null);
INSERT INTO `t_channel` VALUES ('DJ882', '8888DJ', '123', null, null);
INSERT INTO `t_channel` VALUES ('DJ883', '6666DJ', '132', null, null);
INSERT INTO `t_channel` VALUES ('DJ884', 'DJ883', '677', null, null);
INSERT INTO `t_channel` VALUES ('DJ885', '8888DJ', '222', null, null);
INSERT INTO `t_channel` VALUES ('DJ886', '8888DJ', '555', null, null);

-- ----------------------------
-- Table structure for t_payinfo
-- ----------------------------
DROP TABLE IF EXISTS `t_payinfo`;
CREATE TABLE `t_payinfo` (
  `tradeNo` varchar(128) NOT NULL COMMENT '交易号',
  `oderId` varchar(128) DEFAULT NULL COMMENT '订单号',
  `account` varchar(128) DEFAULT NULL COMMENT '账号',
  `createTime` datetime DEFAULT NULL COMMENT '充值时间',
  `money` int(32) DEFAULT NULL COMMENT 'money',
  `gold` int(32) DEFAULT NULL COMMENT '元宝',
  `orderId` varchar(128) DEFAULT NULL COMMENT '订单号',
  `server` varchar(128) DEFAULT NULL COMMENT '区组',
  `status` tinyint(8) DEFAULT NULL COMMENT '订单状态',
  `channelCode` varchar(128) DEFAULT NULL COMMENT '渠道',
  `notifyCount` int(32) DEFAULT NULL COMMENT '已经通知次数',
  `nextNotifyTime` bigint(64) DEFAULT NULL COMMENT '下次通知时间戳',
  `roleUid` bigint(64) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`tradeNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_payinfo
-- ----------------------------
INSERT INTO `t_payinfo` VALUES ('1', '1', 'aaa', '2019-10-15 23:28:07', '121245', '45', '1', '飞狐外传', null, '2222DJ', null, null, null);
INSERT INTO `t_payinfo` VALUES ('2', '2', 'bbb', '2019-10-08 23:28:52', '101000', '445', null, '飞狐外传', null, '6666DJ', null, null, null);
INSERT INTO `t_payinfo` VALUES ('3', '3', 'ccc', '2019-10-01 23:29:23', '45', '45', null, '连城诀', null, 'DJ882', null, null, null);
INSERT INTO `t_payinfo` VALUES ('4', '4', 'fff', '2019-10-04 23:29:58', '222222222', '345', null, '连城诀', null, 'DJ886', null, null, null);

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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_server
-- ----------------------------
INSERT INTO `t_server` VALUES ('1', '飞狐外传', '127.0.0.1', '3307', '10081');
INSERT INTO `t_server` VALUES ('2', '雪山飞狐', '127.0.0.1', '3308', '10082');
INSERT INTO `t_server` VALUES ('3', '连城诀', '127.0.0.1', '3309', '10083');

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
INSERT INTO `user` VALUES ('1', 'vue', 'system', '123456', '1', 'hello@qq.com', ' ', '2017-12-08 09:30:22');
INSERT INTO `user` VALUES ('3', 'springboot', '萌妹子', '123456', '1', 'world@qq.com', ' ', '2017-12-24 06:30:46');
INSERT INTO `user` VALUES ('7', 'admin', '系统管理员', '123456', '1', 'java@qq.com', ' ', '2017-12-21 13:30:29');
