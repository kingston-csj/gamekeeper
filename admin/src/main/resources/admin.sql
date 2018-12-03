/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-12-03 10:32:24
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles_user
-- ----------------------------
INSERT INTO `roles_user` VALUES ('8', '2', '7');
INSERT INTO `roles_user` VALUES ('9', '1', '7');
INSERT INTO `roles_user` VALUES ('17', '3', '7');

-- ----------------------------
-- Table structure for serverinfo
-- ----------------------------
DROP TABLE IF EXISTS `serverinfo`;
CREATE TABLE `serverinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `ip` text COMMENT 'md文件源码',
  `port` int(11) DEFAULT NULL,
  `openDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cid` (`port`)
) ENGINE=InnoDB AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of serverinfo
-- ----------------------------
INSERT INTO `serverinfo` VALUES ('117', '1001(飞狐外传)', '127.0.0.1', '58', '2018-11-28 17:52:50');
INSERT INTO `serverinfo` VALUES ('118', '1002(雪山飞狐)', '127.0.0.1', '64', '2017-12-23 21:42:59');
INSERT INTO `serverinfo` VALUES ('119', '1003(连城诀)', '127.0.0.1', '61', '2017-12-24 09:00:05');
INSERT INTO `serverinfo` VALUES ('120', '1004(天龙八部)', '127.0.0.1', '58', '2017-12-24 10:10:33');
INSERT INTO `serverinfo` VALUES ('121', '1005(射雕英雄传)', '127.0.0.1', '58', '2017-12-24 22:32:20');

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
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'vue', 'system', '202cb962ac59075b964b07152d234b70', '1', 'hello@qq.com', ' ', '2017-12-08 09:30:22');
INSERT INTO `user` VALUES ('3', 'springboot', '萌妹子', '202cb962ac59075b964b07152d234b70', '1', 'world@qq.com', ' ', '2017-12-24 06:30:46');
INSERT INTO `user` VALUES ('7', 'admin', '系统管理员', '202cb962ac59075b964b07152d234b70', '1', 'java@qq.com', ' ', '2017-12-21 13:30:29');
