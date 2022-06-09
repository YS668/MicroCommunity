/*
Navicat MySQL Data Transfer

Source Server         : win10
Source Server Version : 50724
Source Host           : 127.0.0.1:3306
Source Database       : retwisapi

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2021-05-25 10:20:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `uname` varchar(50) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL,
  `content` varchar(50) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) DEFAULT NULL,
  `uname` varchar(30) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `content` varchar(50) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for edit_post
-- ----------------------------
DROP TABLE IF EXISTS `edit_post`;
CREATE TABLE `edit_post` (
  `id` int(11) NOT NULL,
  `content` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for mention
-- ----------------------------
DROP TABLE IF EXISTS `mention`;
CREATE TABLE `mention` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `title` varchar(30) DEFAULT NULL,
  `descr` varchar(50) DEFAULT NULL,
  `content` longtext,
  `time` datetime DEFAULT NULL,
  `status` int(255) DEFAULT '1',
  `type_id` int(11) DEFAULT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `to_id` int(11) DEFAULT NULL,
  `message` varchar(100) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `type_id` int(11) DEFAULT NULL,
  `status` int(255) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for msg_type
-- ----------------------------
DROP TABLE IF EXISTS `msg_type`;
CREATE TABLE `msg_type` (
  `id` int(11) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(30) CHARACTER SET utf8 DEFAULT NULL,
  `descr` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `content` longtext CHARACTER SET utf8,
  `create_time` datetime DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `type_id` int(11) DEFAULT NULL,
  `author` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for report
-- ----------------------------
DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_uid` int(255) DEFAULT NULL,
  `to_id` int(11) DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `type` int(255) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for syslogger
-- ----------------------------
DROP TABLE IF EXISTS `syslogger`;
CREATE TABLE `syslogger` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `descr` varchar(20) DEFAULT NULL,
  `start_time` datetime NOT NULL COMMENT '创建时间',
  `ip` varchar(30) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL,
  `url` varchar(100) NOT NULL COMMENT '修改时间',
  `request_type` varchar(10) NOT NULL DEFAULT '' COMMENT '执行的方法-类全命名.方法',
  `class_method` varchar(100) DEFAULT NULL,
  `params` text NOT NULL COMMENT '传入的参数',
  `response` text NOT NULL COMMENT '主机',
  `exce_time` int(11) DEFAULT NULL,
  `log_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for type
-- ----------------------------
DROP TABLE IF EXISTS `type`;
CREATE TABLE `type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `descr` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(25) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `salt` varchar(10) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `avatar` varchar(100) DEFAULT NULL,
  `private_info` varchar(50) DEFAULT NULL,
  `sex` int(11) DEFAULT '0',
  `status` int(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
INSERT INTO `msg_type` VALUES ('1', '评论');
INSERT INTO `msg_type` VALUES ('2', '回复');
INSERT INTO `msg_type` VALUES ('3', '提到');
INSERT INTO `msg_type` VALUES ('4', '点赞');
INSERT INTO `msg_type` VALUES ('5', '关注');
INSERT INTO `type` VALUES ('1', '学习', null);
INSERT INTO `type` VALUES ('2', '生活', null);
INSERT INTO `type` VALUES ('3', '美食', null);
INSERT INTO `type` VALUES ('4', '旅游', null);
INSERT INTO `type` VALUES ('5', '想法', null);
INSERT INTO `type` VALUES ('6', '其他', null);
